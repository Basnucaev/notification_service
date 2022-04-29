package com.notification.service.root.service.implementation;

import com.notification.service.root.entity.Client;
import com.notification.service.root.entity.Mailing;
import com.notification.service.root.entity.Message;
import com.notification.service.root.entity.enumeration.SentStatus;
import com.notification.service.root.entity.Statistic;
import com.notification.service.root.feigh.clients.ApiResponse;
import com.notification.service.root.feigh.clients.Msg;
import com.notification.service.root.feigh.clients.SendMessagesClient;
import com.notification.service.root.repository.ClientRepository;
import com.notification.service.root.repository.MailingRepository;
import com.notification.service.root.repository.MessageRepository;
import com.notification.service.root.repository.StatisticRepository;
import com.notification.service.root.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final ClientRepository clientRepository;
    private final SendMessagesClient sendMessagesClient;
    private final MessageRepository messageRepository;
    private final StatisticRepository statisticRepository;
    private final MailingRepository mailingRepository;
    private final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    public NotificationServiceImpl(ClientRepository clientRepository, SendMessagesClient sendMessagesClient,
                                   MessageRepository messageRepository, StatisticRepository statisticRepository,
                                   MailingRepository mailingRepository) {
        this.clientRepository = clientRepository;
        this.sendMessagesClient = sendMessagesClient;
        this.messageRepository = messageRepository;
        this.statisticRepository = statisticRepository;
        this.mailingRepository = mailingRepository;
    }

    @Override
    public List<Client> getClientsWhoseOperatorCodeMatchesMailingOperatorCode(String operatorCode) {
        logger.info("method \"getClientsWhoseOperatorCodeMatchesMailingOperatorCode\" | " +
                "returned list of clients");
        return clientRepository.getClientByMobileOperatorCodeEquals(operatorCode);
    }

    @Override
    public void startMailing(List<Client> clients, Mailing mailing) {
        Statistic statistic = new Statistic();
        statistic.setMailing(mailing);
        logger.info("method \"startMailing\" | created statistic for current mailing with id= " + mailing.getId());

        if (!clients.isEmpty()) {
            statistic.setAllMessages(clients.size());
            logger.info("method \"startMailing\" | clients not empty, assigned statistic all need to sent messages= "
                    + statistic.getAllMessages());

            for (Client client : clients) {

                if (compareMailingEndDateWithCurrentDate(mailing, statistic)) {
                    return;
                }

                if (checkIsTheMessageHasBeenSent(client, mailing)) {
                    continue;
                }

                doRequestWithMessageToExternalApi(mailing, client, statistic);
            }
            if (statistic.getUnsentMessages() > 0) {
                mailing.setSentStatus(SentStatus.UNSENT);
            } else {
                mailing.setSentStatus(SentStatus.SENT);
            }
        } else {
            mailing.setSentStatus(SentStatus.NO_CLIENTS_FOR_SENDING);
        }
        statisticRepository.save(statistic);
        logger.info("method \"startMailing\" | statistic for mailing with id= " + mailing.getId() + " was saved");

        mailingRepository.save(mailing);
        logger.info("method \"startMailing\" | mailing with id= " + mailing.getId() + " was updated and finally saved");
    }

    private void doRequestWithMessageToExternalApi(Mailing mailing, Client client, Statistic statistic) {
        Message message = new Message(LocalDateTime.now(), false, mailing, client);
        Message messageWithId = messageRepository.save(message);
        logger.info("method \"startMailing\" | created message for client with id= " + client.getId() +
                " in mailing with id= " + mailing.getId());

        Msg msg = new Msg(messageWithId.getId(), mailing.getTextOfMessage(), client.getPhoneNumber());
        ApiResponse apiResponse;

        try {
            apiResponse = sendMessagesClient.sendMessage(msg.getId(), msg);
        } catch (Exception e) {
            statistic.setUnsentMessages(statistic.getUnsentMessages() + 1);
            messageRepository.save(messageWithId);
            logger.info("method \"startMailing\" | some thing went wrong with external API, current message" +
                    " with id= " + messageWithId.getId() + " not sent, continue the work of the program");
            return;
        }

        if (apiResponse.getMessage().is2xxSuccessful()) {
            messageWithId.setHasBeenSent(true);
            statistic.setSentMessages(statistic.getSentMessages() + 1);
            messageRepository.save(messageWithId);
            logger.info("method \"startMailing\" | got successful response from external API ," +
                    " message with id= " + message.getId() + " was sent");
        } else {
            statistic.setUnsentMessages(statistic.getUnsentMessages() + 1);
            messageRepository.save(messageWithId);
            logger.info("method \"startMailing\" | got error response from external API ," +
                    " message with id= " + message.getId() + " wasn't sent");
        }
    }

    private boolean compareMailingEndDateWithCurrentDate(Mailing mailing, Statistic statistic) {
        long mailingID = mailing.getId();

        // если дата конца рассылки наступила до отправки всех сообщений, установить что рассылка
        // не отправлена, сохранить статистику и выйти из метода
        if (mailing.getEndMailingDate().compareTo(LocalDateTime.now()) < 0) {
            logger.info("method \"startMailing\" | mailing end date earlier than current time "
                    + mailing.getEndMailingDate());
            if (statistic.getSentMessages() == 0 && statistic.getUnsentMessages() == 0) {
                statistic.setUnsentMessages(statistic.getAllMessages());
                logger.info("method \"startMailing\" | mailing end date was incorrect by start, " +
                        "not a single message was sent");
            }
            mailing.setSentStatus(SentStatus.ERROR);
            statisticRepository.save(statistic);
            logger.info("method \"startMailing\" | statistic for mailing with id= " + mailingID +
                    " was saved");
            mailingRepository.save(mailing);
            logger.info("method \"startMailing\" | mailing sent status with id= " + mailingID +
                    " was updated ERROR and finally saved");
            return true;
        } else {
            return false;
        }
    }

    // каждую минуту проверяет, есть ли рассылки у которых дата начала еще не наступила
    @Scheduled(fixedRate = 60000)
    public void checkForUnsentMailings() {
        List<Mailing> mailings = mailingRepository.findAllBySentStatusIsUNSENTAndTimeHasCome(LocalDateTime.now());

        if (mailings.isEmpty()) {
            return;
        }
        logger.info("method \"checkForUnsentMailings\" | founded mailings whose start time has come and sent status" +
                " is UNSENT");
        for (Mailing mailing : mailings) {
            String operatorCode = mailing.getMobileOperatorCode();
            List<Client> clients = getClientsWhoseOperatorCodeMatchesMailingOperatorCode(operatorCode);
            startMailing(clients, mailing);
        }
    }

    private boolean checkIsTheMessageHasBeenSent(Client client, Mailing mailing) {
        Message message = messageRepository.findMessageByClientIdAndMailingId(client.getId(), mailing.getId());
        logger.info("method \"startMailing\" | client with id= " + client.getId() + " already " +
                "got message from mailing with id= " + mailing.getId() + ", step up to next client");
        return message != null && message.isHasBeenSent();
    }
}
