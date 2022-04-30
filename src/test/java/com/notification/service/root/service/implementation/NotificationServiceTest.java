package com.notification.service.root.service.implementation;

import com.notification.service.root.entity.Client;
import com.notification.service.root.entity.Mailing;
import com.notification.service.root.entity.Message;
import com.notification.service.root.entity.enumeration.SentStatus;
import com.notification.service.root.feign.clients.ApiResponse;
import com.notification.service.root.feign.clients.SendMessagesClient;
import com.notification.service.root.repository.ClientRepository;
import com.notification.service.root.repository.MailingRepository;
import com.notification.service.root.repository.MessageRepository;
import com.notification.service.root.repository.StatisticRepository;
import com.notification.service.root.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    private NotificationService notificationService;

    @Mock
    private MailingRepository mailingRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private SendMessagesClient sendMessagesClient;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private StatisticRepository statisticRepository;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationServiceImpl(clientRepository, sendMessagesClient, messageRepository,
                statisticRepository, mailingRepository);
    }


    @Test
    void shouldGetClientsWhoseOperatorCodeMatchesMailingOperatorCode() {
        // given
        List<Client> clients = List.of(new Client("0", "0", "0",
                Collections.emptyList()));
        when(clientRepository.getClientByMobileOperatorCodeEquals(anyString())).thenReturn(clients);

        // when
        List<Client> expected = notificationService.getClientsWhoseOperatorCodeMatchesMailingOperatorCode(anyString());

        // then
        verify(clientRepository).getClientByMobileOperatorCodeEquals(anyString());
        assertThat(expected).isEqualTo(clients);
    }

    @Test
    void startMailingWhenEverythingIsOk() {
        // given
        Mailing mailing = new Mailing(LocalDateTime.now().minusHours(1L), "0", "0",
                LocalDateTime.now().plusHours(1L), SentStatus.SENT, Collections.emptyList());
        mailing.setId(1L);

        Client client = new Client("0", "0", "0",
                Collections.emptyList());

        List<Client> clients = List.of(client);

        Message messageWithId = new Message(LocalDateTime.now(), false, mailing, client);
        messageWithId.setId(1L);

        ApiResponse apiResponse = new ApiResponse(1, HttpStatus.OK);

        when(messageRepository.save(any())).thenReturn(messageWithId);
        when(sendMessagesClient.sendMessage(anyLong(), any())).thenReturn(apiResponse);

        // when
        notificationService.startMailing(clients, mailing);

        // then
        verify(statisticRepository, atMostOnce()).save(any());
        verify(mailingRepository, atMostOnce()).save(mailing);
        verify(sendMessagesClient, atLeastOnce()).sendMessage(anyLong(), any());
    }

    @Test
    void startMailingWhenMailingEndDateEarlierThanCurrentDate() {
        // given
        Mailing mailing = new Mailing(LocalDateTime.now().minusHours(2L), "0", "0",
                LocalDateTime.now().minusHours(1L), SentStatus.SENT, Collections.emptyList());
        mailing.setId(1L);

        Client client = new Client("0", "0", "0",
                Collections.emptyList());
        List<Client> clients = List.of(client);

        // when
        notificationService.startMailing(clients, mailing);

        // then
        verify(statisticRepository, atMostOnce()).save(any());
        verify(mailingRepository, atMostOnce()).save(mailing);
        verify(sendMessagesClient, never()).sendMessage(anyLong(), any());
    }

    @Test
    void shouldStartMailingWhenMailingStartTimeHasCome() {
        // given
        List<Mailing> mailings = List.of(new Mailing(LocalDateTime.now().minusHours(1L), "0", "0",
                LocalDateTime.now().plusHours(1L), SentStatus.SENT, Collections.emptyList()));
        List<Client> clients = List.of(new Client("0", "0", "0",
                Collections.emptyList()));

        when(mailingRepository.findAllBySentStatusIsUNSENTAndTimeHasCome(any())).thenReturn(mailings);

        // when
        notificationService.checkForUnsentMailings();

        // then

    }
}