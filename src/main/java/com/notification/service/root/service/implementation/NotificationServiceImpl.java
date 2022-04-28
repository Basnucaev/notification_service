package com.notification.service.root.service.implementation;

import com.notification.service.root.entity.Client;
import com.notification.service.root.entity.Mailing;
import com.notification.service.root.entity.Message;
import com.notification.service.root.entity.Statistic;
import com.notification.service.root.feigh.clients.ApiResponse;
import com.notification.service.root.feigh.clients.Msg;
import com.notification.service.root.feigh.clients.SendMessagesClient;
import com.notification.service.root.repository.ClientRepository;
import com.notification.service.root.repository.MailingRepository;
import com.notification.service.root.repository.MessageRepository;
import com.notification.service.root.repository.StatisticRepository;
import com.notification.service.root.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final ClientRepository clientRepository;
    private final SendMessagesClient sendMessagesClient;
    private final MessageRepository messageRepository;
    private final StatisticRepository statisticRepository;
    private final MailingRepository mailingRepository;

    @Autowired
    public NotificationServiceImpl(ClientRepository clientRepository, SendMessagesClient sendMessagesClient,
                                   MessageRepository messageRepository, StatisticRepository statisticRepository, MailingRepository mailingRepository) {
        this.clientRepository = clientRepository;
        this.sendMessagesClient = sendMessagesClient;
        this.messageRepository = messageRepository;
        this.statisticRepository = statisticRepository;
        this.mailingRepository = mailingRepository;
    }

    @Override
    public List<Client> getClientsWhoseOperatorCodeMatchesMailingOperatorCode(String operatorCode) {
        return clientRepository.getClientByMobileOperatorCodeEquals(operatorCode);
    }

    @Override
    public void startMailing(List<Client> clients, Mailing mailing) {
        Statistic statistic = new Statistic();
        statistic.setMailing(mailing);

        if (!clients.isEmpty()) {
            statistic.setAllMessages(clients.size());

            for (Client client : clients) {
                /*
                 если дата конца рассылки наступила до отправки всех сообщений, установить что рассылка
                 не отправлена, сохранить статистику и выйти из метода
                 */
                if (mailing.getEndMailingDate().compareTo(LocalDateTime.now()) < 0) {
                    if (statistic.getSentMessages() == 0 && statistic.getUnsentMessages() == 0) {
                        statistic.setUnsentMessages(statistic.getAllMessages());
                    }
                    mailing.setSent(false);
                    statisticRepository.save(statistic);
                    mailingRepository.save(mailing);
                    return;
                }

                // проверяет отсылались ли уже клиенту письма с этой рассылки
                if (checkIsTheMessageHasBeenSent(client, mailing)) {
                    continue;
                }

                Message message = new Message(LocalDateTime.now(), false, mailing, client);
                Message messageWithId = messageRepository.save(message);
                Msg msg = new Msg();
                msg.setId(messageWithId.getId().intValue());
                msg.setText(mailing.getTextOfMessage());
                msg.setPhone(client.getPhoneNumber());

                ApiResponse apiResponse = sendMessagesClient.sendMessage(msg.getId(), msg);
                if (apiResponse.getMessage().is2xxSuccessful()) {
                    messageWithId.setHasBeenSent(true);
                    statistic.setSentMessages(statistic.getSentMessages() + 1);
                    messageRepository.save(messageWithId);
                } else {
                    statistic.setUnsentMessages(statistic.getUnsentMessages() + 1);
                }
            }
        }
        mailing.setSent(true);
        statisticRepository.save(statistic);
        mailingRepository.save(mailing);
    }

    // каждую минуту проверяет, есть ли рассылки у которых дата начала еще не наступила
    @Scheduled(fixedRate = 60000)
    public void checkForUnsentMailings() {
        List<Mailing> mailings = mailingRepository.findAllBySentIsFalseAndTimeHasCome(LocalDateTime.now());

        if (mailings.isEmpty()) {
            return;
        }
        for (Mailing mailing : mailings) {
            String operatorCode = mailing.getMobileOperatorCode();
            List<Client> clients = getClientsWhoseOperatorCodeMatchesMailingOperatorCode(operatorCode);
            startMailing(clients, mailing);
        }
    }

    private boolean checkIsTheMessageHasBeenSent(Client client, Mailing mailing) {
        Message message = messageRepository.findMessageByClientIdAndMailingId(client.getId(), mailing.getId());
        return message != null && message.isHasBeenSent();
    }
}
