package com.notification.service.root.service.notification;

import com.notification.service.root.entity.Client;
import com.notification.service.root.entity.Mailing;
import com.notification.service.root.entity.enumeration.SentStatus;
import com.notification.service.root.repository.MailingRepository;
import com.notification.service.root.service.NotificationService;
import com.notification.service.root.service.implementation.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailingNotificationTest {
    MailingNotification mailingNotification;

    @Mock
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        mailingNotification = new MailingNotification(notificationService);
    }

    @Test
    void shouldNotifyClientsImmediatelyWhenStartTimeEarlierThanCurrentTime() {
        // given
        List<Client> clients = List.of(new Client("0", "0", "worker",
                Collections.emptyList()));

        Mailing mailing = new Mailing(LocalDateTime.now().minusHours(1), "0", "0",
                LocalDateTime.now(), SentStatus.SENT, Collections.emptyList());

        when(notificationService.getClientsWhoseOperatorCodeMatchesMailingOperatorCode(anyString()))
                .thenReturn(clients);

        // when
        mailingNotification.notifyClients(mailing);

        // then
        verify(notificationService).getClientsWhoseOperatorCodeMatchesMailingOperatorCode(anyString());
        verify(notificationService).startMailing(clients,mailing);
    }

    @Test
    void shouldNotNotifyClientsImmediatelyWhenStartTimeLaterThanCurrentTime() {
        // given
        List<Client> clients = List.of(new Client("0", "0", "worker",
                Collections.emptyList()));

        Mailing mailing = new Mailing(LocalDateTime.now().plusHours(1), "0", "0",
                LocalDateTime.now(), SentStatus.SENT, Collections.emptyList());

        when(notificationService.getClientsWhoseOperatorCodeMatchesMailingOperatorCode(anyString()))
                .thenReturn(clients);

        // when
        mailingNotification.notifyClients(mailing);

        // then
        verify(notificationService).getClientsWhoseOperatorCodeMatchesMailingOperatorCode(anyString());
        verify(notificationService, never()).startMailing(clients,mailing);
    }
}