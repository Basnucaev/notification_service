package com.notification.service.root.controller;

import com.notification.service.root.entity.Mailing;
import com.notification.service.root.entity.enumeration.SentStatus;
import com.notification.service.root.service.MailingService;
import com.notification.service.root.service.notification.MailingNotification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MailingController.class)
public class MailingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MailingService mailingService;
    @MockBean
    private MailingNotification mailingNotification;

    private final String uri = "http://localhost:8080/api/v1/mailing";
    private final String mailingJSON =
            "{" +
                    "\"id\": 1," +
                    "\"startMailingDate\": \"2022-04-28T15:18:55\"," +
                    "\"textOfMessage\": \"hello\"," +
                    "\"mobileOperatorCode\": \"321\"," +
                    "\"endMailingDate\": \"2021-04-23T18:25:43\"," +
                    "\"message\": []," +
                    "\"sentStatus\": \"SENT\"" +
                    "}";

    private final String savemailingJSON =
            "{" +
                    "\"startMailingDate\": \"2022-04-28T15:18:55\"," +
                    "\"textOfMessage\": \"hello\"," +
                    "\"mobileOperatorCode\": \"321\"," +
                    "\"endMailingDate\": \"2021-04-23T18:25:43\"," +
                    "\"message\": []," +
                    "\"sentStatus\": \"SENT\"" +
                    "}";

    private final String mailingsJSON =
            "[" +
                    "{" +
                    "\"startMailingDate\": \"2022-04-28T15:18:55\"," +
                    "\"textOfMessage\": \"hello\"," +
                    "\"mobileOperatorCode\": \"321\"," +
                    "\"endMailingDate\": \"2021-04-23T18:25:43\"," +
                    "\"message\": []," +
                    "\"sentStatus\": \"SENT\"" +
                    "}" +
                    "]";

    @Test
    void getMailingByIdShouldReturnJsonAndStatusIsOK() throws Exception {
        // given
        Mailing mailing = new Mailing(LocalDateTime.parse("2022-04-28T15:18:55"), "hello",
                "321", LocalDateTime.parse("2021-04-23T18:25:43"), SentStatus.SENT,
                Collections.emptyList());
        mailing.setId(1L);
        when(mailingService.getMailingById(mailing.getId())).thenReturn(mailing);

        // when / then
        mockMvc.perform(MockMvcRequestBuilders.get(uri + "/1"))
                .andExpect(content().json(mailingJSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllMailingsShouldReturnJsonAndStatusIsOK() throws Exception {
        // given
        List<Mailing> mailings = List.of(new Mailing(LocalDateTime.parse("2022-04-28T15:18:55"), "hello",
                "321", LocalDateTime.parse("2021-04-23T18:25:43"),
                SentStatus.SENT, Collections.emptyList()));
        when(mailingService.getAllMailings()).thenReturn(mailings);

        // when / then
        mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(content().json(mailingsJSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldSaveMailingAndReturnStatusIsCreated() throws Exception {
        // given

        // when / then
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .content(savemailingJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(savemailingJSON))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateMailingAndReturnStatusIsOk() throws Exception {
        // given

        // when / then
        mockMvc.perform(MockMvcRequestBuilders.put(uri)
                        .content(mailingJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteMailingById() throws Exception {
        // given
        int id = 1;

        // when / then
        mockMvc.perform(MockMvcRequestBuilders.delete(uri + "/" + id))
                .andExpect(content().string("Mailing with id= " + id + " was deleted")).
                andExpect(status().isOk());
    }
}
