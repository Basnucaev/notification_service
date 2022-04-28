package com.notification.service.root.service;

import com.notification.service.root.entity.Client;
import com.notification.service.root.entity.Mailing;

import java.util.List;

public interface NotificationService {

    List<Client> getClientsWhoseOperatorCodeMatchesMailingOperatorCode(String operatorCode);

    void startMailing(List<Client> clients, Mailing mailing);
}
