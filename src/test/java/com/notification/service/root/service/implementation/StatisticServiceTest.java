package com.notification.service.root.service.implementation;

import com.notification.service.root.entity.Mailing;
import com.notification.service.root.entity.Statistic;
import com.notification.service.root.entity.enumeration.SentStatus;
import com.notification.service.root.repository.MailingRepository;
import com.notification.service.root.repository.StatisticRepository;
import com.notification.service.root.service.StatisticService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticServiceTest {
    StatisticService statisticService;

    @Mock
    private MailingRepository mailingRepository;
    @Mock
    private StatisticRepository statisticRepository;

    @BeforeEach
    void setUp() {
        statisticService = new StatisticServiceImpl(mailingRepository, statisticRepository);
    }

    @Test
    void shouldGetStatisticForAllMailings() {
        // given
        List<Statistic> statistics = List.of(new Statistic(1, 0, 1, new Mailing()));
        when(statisticRepository.findAll()).thenReturn(statistics);

        // when
        List<Statistic> expected = statisticService.getStatisticForAllMailings();

        // then
        verify(statisticRepository).findAll();
        assertThat(expected).isEqualTo(statistics);
    }

    @Test
    void shouldGetStatisticByMailingId() {
        // given
        Statistic statistic = new Statistic(1, 0, 1, new Mailing());
        Mailing mailing = new Mailing(LocalDateTime.now(), "0", "0", LocalDateTime.now(),
                SentStatus.SENT, Collections.emptyList());

        when(mailingRepository.findById(anyLong())).thenReturn(Optional.of(mailing));
        when(statisticRepository.findByMailingId(anyLong())).thenReturn(statistic);

        // when
        Statistic expected = statisticService.getStatisticByMailingId(anyLong());

        // then
        verify(statisticRepository).findByMailingId(anyLong());
        verify(mailingRepository).findById(anyLong());
        assertThat(expected).isEqualTo(statistic);
    }
}