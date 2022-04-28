package com.notification.service.root.service.notification;

import com.notification.service.root.entity.Client;
import com.notification.service.root.entity.Mailing;
import com.notification.service.root.service.MailingSubject;
import com.notification.service.root.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MailingNotification implements MailingSubject {
    private List<Client> clients;
    private Mailing currentMailing;

    private final NotificationService notificationService;

    @Autowired
    public MailingNotification(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void registerClient(Client client) {
        clients.add(client);
    }

    @Override
    public void removeClient(Client client) {
        clients.remove(client);
    }

    @Override
    public void notifyClients(Mailing mailing) {
        // получить клиентов по коду оператора указанного в рассылке
        String operatorCode = mailing.getMobileOperatorCode();
        clients = notificationService.getClientsWhoseOperatorCodeMatchesMailingOperatorCode(operatorCode);

        // если время рассылки в прошлом или наступило только-что, то начать рассылку
        if (mailing.getStartMailingDate().compareTo(LocalDateTime.now()) <= 0)
            notificationService.startMailing(clients, mailing);

        // если время рассылки еще не наступило, ничего не делать, метод с аннотацией @Scheduled - сам все сделает
    }
}
