package com.notification.service.root.controller;

import com.notification.service.root.entity.Mailing;
import com.notification.service.root.entity.Statistic;
import com.notification.service.root.entity.enumeration.SentStatus;
import com.notification.service.root.service.StatisticService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(StatisticController.class)
class StatisticControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticService statisticService;

    private final String uri = "http://localhost:8080/api/v1/statistics";
    private final String statisticJSON =
            "{\n" +
                    "\"id\": 1,\n" +
                    "\"sentMessages\": 0,\n" +
                    "\"unsentMessages\": 5,\n" +
                    "\"allMessages\": 5,\n" +
                    "\"mailing\": {\n" +
                    "     \"id\": 1,\n" +
                    "     \"startMailingDate\": \"2022-04-28T15:18:55\",\n" +
                    "     \"textOfMessage\": \"hello\",\n" +
                    "     \"mobileOperatorCode\": \"321\",\n" +
                    "     \"endMailingDate\": \"2021-04-23T18:25:43\",\n" +
                    "     \"message\": [],\n" +
                    "     \"sent\": \"SENT\"\n" +
                    "     }\n" +
                    "}";
    private final String statisticsJSON =
            "[\n" +
                    "{\n" +
                    "     \"id\": 1,\n" +
                    "     \"sentMessages\": 0,\n" +
                    "     \"unsentMessages\": 5,\n" +
                    "     \"allMessages\": 5,\n" +
                    "     \"mailing\": {\n" +
                    "          \"id\": 1,\n" +
                    "          \"startMailingDate\": \"2022-04-28T15:18:55\",\n" +
                    "          \"textOfMessage\": \"hello\",\n" +
                    "          \"mobileOperatorCode\": \"321\",\n" +
                    "          \"endMailingDate\": \"2021-04-23T18:25:43\",\n" +
                    "          \"message\": [],\n" +
                    "          \"sent\": \"SENT\"\n" +
                    "     }\n" +
                    "}\n" +
                    "]";

    @Test
    void getStatisticByMailingIdShouldReturnJsonAndStatusIsOK() throws Exception {
        // given
        Mailing mailing = new Mailing(LocalDateTime.parse("2022-04-28T15:18:55"), "hello",
                "321", LocalDateTime.parse("2021-04-23T18:25:43"), SentStatus.SENT,
                Collections.emptyList());
        mailing.setId(1L);

        Statistic statistic = new Statistic(0, 5, 5, mailing);
        statistic.setId(1L);
        when(statisticService.getStatisticByMailingId(mailing.getId())).thenReturn(statistic);

        // when / then
        mockMvc.perform(MockMvcRequestBuilders.get(uri + "/1"))
                .andExpect(content().json(statisticJSON))
                .andExpect(status().isOk());
    }

    @Test
    void getStatisticForAllMailingsShouldReturnJsonAndStatusIsOK() throws Exception {
        // given
        Mailing mailing = new Mailing(LocalDateTime.parse("2022-04-28T15:18:55"), "hello",
                "321", LocalDateTime.parse("2021-04-23T18:25:43"), SentStatus.SENT,Collections.emptyList());
        mailing.setId(1L);

        Statistic statistic = new Statistic(0, 5, 5, mailing);
        statistic.setId(1L);

        List<Statistic> statistics = List.of(statistic);
        when(statisticService.getStatisticForAllMailings()).thenReturn(statistics);

        // when / then
        mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(content().json(statisticsJSON))
                .andExpect(status().isOk());
    }
}