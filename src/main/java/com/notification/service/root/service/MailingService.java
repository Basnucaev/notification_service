package com.notification.service.root.service;

import com.notification.service.root.entity.Mailing;

import java.util.List;

public interface MailingService {
    Mailing getMailingById(Long id);

    List<Mailing> getAllMailings();

    void saveMailing(Mailing mailing);

    void updateMailing(Mailing mailing);

    void deleteMailingById(Long id);
}
