package com.notification.service.root.controller;

import com.notification.service.root.entity.Mailing;
import com.notification.service.root.service.MailingService;
import com.notification.service.root.service.notification.MailingNotification;
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
public class MailingController {
    private final MailingService mailingService;
    private final MailingNotification mailingNotification;

    @Autowired
    public MailingController(MailingService mailingService, MailingNotification mailingNotification) {
        this.mailingService = mailingService;
        this.mailingNotification = mailingNotification;
    }


    @GetMapping("/mailing/{id}")
    public ResponseEntity<Mailing> getMailingById(@PathVariable Long id) {
        Mailing mailing = mailingService.getMailingById(id);
        return new ResponseEntity<>(mailing, HttpStatus.OK);
    }

    @GetMapping("/mailing")
    public ResponseEntity<List<Mailing>> getAllMailings() {
        List<Mailing> mailings = mailingService.getAllMailings();
        return new ResponseEntity<>(mailings, HttpStatus.OK);
    }

    @PostMapping("/mailing")
    public ResponseEntity<Mailing> saveMailing(@RequestBody Mailing mailing) {
        // сохраняет рассылку в базе данных
        mailingService.saveMailing(mailing);

        // начинает рассылку пользователям, код мобильного оператора которых совпадает с кодом рассылки
        mailingNotification.notifyClients(mailing);
        return new ResponseEntity<>(mailing, HttpStatus.CREATED);
    }

    @PutMapping("/mailing")
    public ResponseEntity<Mailing> updateMailing(@RequestBody Mailing mailing) {
        mailingService.updateMailing(mailing);
        return new ResponseEntity<>(mailing, HttpStatus.OK);
    }

    @DeleteMapping("/mailing/{id}")
    public ResponseEntity<String> deleteMailingById(@PathVariable Long id) {
        mailingService.deleteMailingById(id);
        return new ResponseEntity<>("Mailing with id= " + id + " was deleted", HttpStatus.OK);
    }
}
