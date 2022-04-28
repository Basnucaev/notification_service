package com.notification.service.root.service;

import com.notification.service.root.entity.Client;
import com.notification.service.root.entity.Mailing;

public interface MailingSubject {
    void registerClient(Client Client);
    void removeClient(Client Client);
    void notifyClients(Mailing mailing);
}
