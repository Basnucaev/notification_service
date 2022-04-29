package com.notification.service.root.service.implementation;

import com.notification.service.root.entity.Client;
import com.notification.service.root.exception.ClientNotFoundException;
import com.notification.service.root.exception.EntityNotSavedException;
import com.notification.service.root.exception.EntityNotUpdatedException;
import com.notification.service.root.repository.ClientRepository;
import com.notification.service.root.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        clientService = new ClientServiceImpl(clientRepository);
    }

    @Test
    void shouldGetClientById() {
        // given
        Client client = new Client("0", "0", "0", Collections.emptyList());
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));

        // when
        Client expected = clientService.getClientById(anyLong());

        // then
        verify(clientRepository).findById(anyLong());
        assertThat(expected).isEqualTo(client);
    }

    @Test
    void shouldGetAllClients() {
        // given
        List<Client> clients = List.of(
                new Client("0", "0", "0", Collections.emptyList()));
        when(clientRepository.findAll()).thenReturn(clients);

        // when
        List<Client> expected = clientService.getAllClients();

        // then
        verify(clientRepository).findAll();
        assertThat(expected).isEqualTo(clients);
    }

    @Test
    void shouldSaveClient() {
        // given
        Client client = new Client("0", "0", "0", Collections.emptyList());
        when(clientRepository.save(client)).thenReturn(client);

        // when
        clientService.saveClient(client);

        // then
        verify(clientRepository).save(client);
    }

    @Test
    void shouldUpdateClient() {
        // given
        Client client = new Client("0", "0", "0", Collections.emptyList());
        client.setId(1L);

        when(clientRepository.save(client)).thenReturn(client);

        // when
        clientService.updateClient(client);

        // then
        verify(clientRepository).save(client);

    }

    @Test
    void shouldDeleteClientById() {
        // given
        Client client = new Client("0", "0", "0", Collections.emptyList());
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));

        // when
        clientService.deleteClientById(anyLong());

        // then
        verify(clientRepository).deleteById(anyLong());
    }

    @Test
    void shouldThrowClientNotFoundExceptionWhenTryingToGetNonExistentClient() {
        // given
        long id = 0;
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> clientService.getClientById(id))
                .isInstanceOf(ClientNotFoundException.class)
                .hasMessageContaining("Client with id= " + id + " not found");
    }

    @Test
    void shouldThrowEntityNotSavedExceptionWhenTryingToSaveEntityWithNotNullId() {
        // given
        Client client = new Client("0", "0", "0", Collections.emptyList());
        client.setId(1L);

        // when / then
        assertThatThrownBy(() -> clientService.saveClient(client))
                .isInstanceOf(EntityNotSavedException.class)
                .hasMessageContaining(" was not saved because id is not null");
    }

    @Test
    void shouldThrowEntityNotUpdatedExceptionWhenTryingToUpdateEntityWithNullId() {
        // given
        Client client = new Client("0", "0", "0", Collections.emptyList());

        // when / then
        assertThatThrownBy(() -> clientService.updateClient(client))
                .isInstanceOf(EntityNotUpdatedException.class)
                .hasMessageContaining(" was not updated because id is null or \"0\"");
    }
}