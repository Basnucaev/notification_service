package com.notification.service.root.controller;

import com.notification.service.root.entity.Client;
import com.notification.service.root.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Client client = clientService.getClientById(id);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @PostMapping("/clients")
    public ResponseEntity<Client> saveClient(@RequestBody Client client) {
        clientService.saveClient(client);
        return new ResponseEntity<>(client, HttpStatus.CREATED);
    }

    @PutMapping("/clients")
    public ResponseEntity<Client> updateClient(@RequestBody Client client) {
        clientService.updateClient(client);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @DeleteMapping("/clients/{id}")
    public ResponseEntity<String> deleteClientById(@PathVariable Long id) {
        clientService.deleteClientById(id);
        return new ResponseEntity<>("Mailing with id= " + id + " was deleted", HttpStatus.OK);
    }
}
