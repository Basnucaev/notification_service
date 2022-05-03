package com.notification.service.root.service.implementation;

import com.notification.service.root.entity.Client;
import com.notification.service.root.exception.ClientNotFoundException;
import com.notification.service.root.exception.EntityNotSavedException;
import com.notification.service.root.exception.EntityNotUpdatedException;
import com.notification.service.root.repository.ClientRepository;
import com.notification.service.root.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    @Override
    public Client getClientById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isPresent()) {
            log.info("method \"getClientById\" | returned client with id= {}", id);
            return client.get();
        } else {
            log.info("method \"getClientById\" | client with id= {} not found", id);
            throw new ClientNotFoundException(id);
        }
    }

    @Override
    public List<Client> getAllClients() {
        log.info("method \"getAllClients\" | returned list of clients");
        return clientRepository.findAll();
    }

    @Override
    public void saveClient(Client client) {
        if (client.getId() == null) {
            clientRepository.save(client);
            log.info("method \"saveClient\" | client saved, assigned id= {}", client.getId());
        } else {
            log.info("method \"saveClient\" | client not saved, client already have id");
            throw new EntityNotSavedException("Client");
        }
    }

    @Override
    public void updateClient(Client client) {
        if (client.getId() != null && client.getId() != 0) {
            clientRepository.save(client);
            log.info("method \"updateClient\" | client with id= {} updated", client.getId());
        } else {
            log.info("method \"updateClient\" | client not updated, client has no id");
            throw new EntityNotUpdatedException("Client");
        }
    }

    @Override
    public void deleteClientById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isPresent()) {
            log.info("method \"deleteClientById\" | client with id= {} deleted", id);
            clientRepository.deleteById(id);
        } else {
            log.info("method \"getClientById\" | client with id= {} not found", id);
            throw new ClientNotFoundException(id);
        }
    }
}
