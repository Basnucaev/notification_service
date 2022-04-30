package com.notification.service.root.service.implementation;

import com.notification.service.root.entity.Mailing;
import com.notification.service.root.entity.enumeration.SentStatus;
import com.notification.service.root.exception.EntityNotSavedException;
import com.notification.service.root.exception.EntityNotUpdatedException;
import com.notification.service.root.exception.MailingNotFoundException;
import com.notification.service.root.repository.MailingRepository;
import com.notification.service.root.service.MailingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailingServiceTest {
    private MailingService mailingService;

    @Mock
    private MailingRepository mailingRepository;

    @BeforeEach
    void setUp() {
        mailingService = new MailingServiceImpl(mailingRepository);
    }

    @Test
    void shouldGetMailingById() {
        // given
        Mailing mailing = new Mailing(LocalDateTime.now(), "0", "0", LocalDateTime.now(),
                SentStatus.SENT, Collections.emptyList());

        when(mailingRepository.findById(anyLong())).thenReturn(Optional.of(mailing));

        // when
        Mailing expected = mailingService.getMailingById(anyLong());

        // then
        verify(mailingRepository).findById(anyLong());
        assertThat(expected).isEqualTo(mailing);
    }

    @Test
    void shouldGetAllMailings() {
        // given
        List<Mailing> mailings = List.of(
                new Mailing(LocalDateTime.now(), "0", "0", LocalDateTime.now(),
                        SentStatus.SENT, Collections.emptyList()));
        when(mailingRepository.findAll()).thenReturn(mailings);

        // when
        List<Mailing> expected = mailingService.getAllMailings();

        // then
        verify(mailingRepository).findAll();
        assertThat(expected).isEqualTo(mailings);
    }

    @Test
    void shouldSaveMailing() {
        // given
        Mailing mailing = new Mailing(LocalDateTime.now(), "0", "0", LocalDateTime.now(),
                SentStatus.SENT, Collections.emptyList());
        when(mailingRepository.save(mailing)).thenReturn(mailing);

        // when
        mailingService.saveMailing(mailing);

        // then
        verify(mailingRepository).save(mailing);
    }

    @Test
    void shouldUpdateMailing() {
        // given
        Mailing mailing = new Mailing(LocalDateTime.now(), "0", "0", LocalDateTime.now(),
                SentStatus.SENT, Collections.emptyList());
        mailing.setId(1L);

        when(mailingRepository.save(mailing)).thenReturn(mailing);

        // when
        mailingService.updateMailing(mailing);

        // then
        verify(mailingRepository).save(mailing);

    }

    @Test
    void shouldDeleteMailingById() {
        // given
        Mailing mailing = new Mailing(LocalDateTime.now(), "0", "0", LocalDateTime.now(),
                SentStatus.SENT, Collections.emptyList());
        when(mailingRepository.findById(anyLong())).thenReturn(Optional.of(mailing));

        // when
        mailingService.deleteMailingById(anyLong());

        // then
        verify(mailingRepository).deleteById(anyLong());
    }

    @Test
    void shouldThrowMailingNotFoundExceptionWhenTryingToGetNonExistentMailing() {
        // given
        long id = 0;
        when(mailingRepository.findById(id)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> mailingService.getMailingById(id))
                .isInstanceOf(MailingNotFoundException.class)
                .hasMessageContaining("Mailing with id= " + id + " not found");
    }

    @Test
    void shouldThrowEntityNotSavedExceptionWhenTryingToSaveEntityWithNotNullId() {
        // given
        Mailing mailing = new Mailing(LocalDateTime.now(), "0", "0", LocalDateTime.now(),
                SentStatus.SENT, Collections.emptyList());
        mailing.setId(1L);

        // when / then
        assertThatThrownBy(() -> mailingService.saveMailing(mailing))
                .isInstanceOf(EntityNotSavedException.class)
                .hasMessageContaining(" was not saved because id is not null");
    }

    @Test
    void shouldThrowEntityNotUpdatedExceptionWhenTryingToUpdateEntityWithNullId() {
        // given
        Mailing mailing = new Mailing(LocalDateTime.now(), "0", "0", LocalDateTime.now(),
                SentStatus.SENT, Collections.emptyList());

        // when / then
        assertThatThrownBy(() -> mailingService.updateMailing(mailing))
                .isInstanceOf(EntityNotUpdatedException.class)
                .hasMessageContaining(" was not updated because id is null or \"0\"");
    }
}