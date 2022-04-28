package com.notification.service.root.service;

import com.notification.service.root.entity.Client;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ClientService {

    Client getClientById(Long id);

    List<Client> getAllClients();

    void saveClient(Client client);

    void updateClient(Client client);

    void deleteClientById(Long id);
}
