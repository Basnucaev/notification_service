package com.notification.service.root.service.implementation;

import com.notification.service.root.entity.Client;
import com.notification.service.root.exception.ClientNotFoundException;
import com.notification.service.root.exception.EntityNotSavedException;
import com.notification.service.root.exception.EntityNotUpdatedException;
import com.notification.service.root.repository.ClientRepository;
import com.notification.service.root.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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
            return client.get();
        } else {
            throw new ClientNotFoundException(id);
        }
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public void saveClient(Client client) {
        if (client.getId() == null) {
            clientRepository.save(client);
        } else {
            throw new EntityNotSavedException("Client");
        }
    }

    @Override
    public void updateClient(Client client) {
        if (client.getId() != null && client.getId() != 0) {
            clientRepository.save(client);
        } else {
            throw new EntityNotUpdatedException("Client");
        }
    }

    @Override
    public void deleteClientById(Long id) {
        // если клиент с таким id не будет найден, пользователь получить соответствующее сообщение
        Client client = getClientById(id);
        clientRepository.deleteById(id);
    }
}
