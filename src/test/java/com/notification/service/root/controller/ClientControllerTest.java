package com.notification.service.root.controller;

import com.notification.service.root.entity.Client;
import com.notification.service.root.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    private final String uri = "http://localhost:8080/api/v1/clients";
    private final String clientJSON =
            "{" +
                    "\"id\" : 1," +
                    "\"phoneNumber\" : \"79660057370\"," +
                    "\"mobileOperatorCode\" : \"7966\"," +
                    "\"teg\" : \"worker\"" +
                    "}";

    private final String saveClientJSON =
            "{" +
                    "\"phoneNumber\" : \"79660057370\"," +
                    "\"mobileOperatorCode\" : \"7966\"," +
                    "\"teg\" : \"worker\"" +
                    "}";

    private final String clientsJSON =
            "[" +
                    "{" +
                    "\"phoneNumber\" : \"79660057370\"," +
                    "\"mobileOperatorCode\" : \"7966\"," +
                    "\"teg\" : \"worker\"" +
                    "}," +
                    "{" +
                    "\"phoneNumber\" : \"79660057370\"," +
                    "\"mobileOperatorCode\" : \"7966\"" +
                    ",\"teg\" : \"worker\"" +
                    "}" +
                    "]";

    @Test
    void getClientByIdShouldReturnJsonAndStatusIsOK() throws Exception {
        // given
        Client client =
                new Client("79660057370", "7966", "worker", Collections.emptyList());
        client.setId(1L);
        when(clientService.getClientById(client.getId())).thenReturn(client);

        // when / then
        mockMvc.perform(MockMvcRequestBuilders.get(uri + "/1"))
                .andExpect(content().json(clientJSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllClientsShouldReturnJsonAndStatusIsOK() throws Exception {
        // given
        List<Client> clients = List.of(
                new Client("79660057370", "7966", "worker", Collections.emptyList()),
                new Client("79660057370", "7966", "worker", Collections.emptyList()));
        when(clientService.getAllClients()).thenReturn(clients);

        // when / then
        mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(content().json(clientsJSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldSaveClientAndReturnStatusIsCreated() throws Exception {
        // given

        // when / then
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .content(saveClientJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(saveClientJSON))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateClientAndReturnStatusIsOk() throws Exception {
        // given

        // when / then
        mockMvc.perform(MockMvcRequestBuilders.put(uri)
                        .content(clientJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteClientById() throws Exception {
        // given
        int id = 1;

        // when / then
        mockMvc.perform(MockMvcRequestBuilders.delete(uri + "/" + id))
                .andExpect(content().string("Client with id= " + id + " was deleted")).
                andExpect(status().isOk());
    }
}