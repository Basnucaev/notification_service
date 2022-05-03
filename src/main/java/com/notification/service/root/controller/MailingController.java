package com.notification.service.root.controller;

import com.notification.service.root.entity.Mailing;
import com.notification.service.root.service.MailingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Mailing Controller", description = "CRUD операции с рассылками")
public class MailingController {
    private final MailingService mailingService;

    @Autowired
    public MailingController(MailingService mailingService) {
        this.mailingService = mailingService;
    }


    @Operation(summary = "Возвращает рассылку по ID")
    @GetMapping("/mailing/{id}")
    public ResponseEntity<Mailing> getMailingById(@PathVariable Long id) {
        Mailing mailing = mailingService.getMailingById(id);
        return new ResponseEntity<>(mailing, HttpStatus.OK);
    }

    @Operation(summary = "Возвращает все рассылки")
    @GetMapping("/mailing")
    public ResponseEntity<List<Mailing>> getAllMailings() {
        List<Mailing> mailings = mailingService.getAllMailings();
        return new ResponseEntity<>(mailings, HttpStatus.OK);
    }

    @Operation(summary = "Добавляет рассылку")
    @PostMapping("/mailing")
    public ResponseEntity<Mailing> saveMailing(@RequestBody Mailing mailing) {
        mailingService.saveMailing(mailing);
        return new ResponseEntity<>(mailing, HttpStatus.CREATED);
    }

    @Operation(summary = "Обновляет рассылку")
    @PutMapping("/mailing")
    public ResponseEntity<Mailing> updateMailing(@RequestBody Mailing mailing) {
        mailingService.updateMailing(mailing);
        return new ResponseEntity<>(mailing, HttpStatus.OK);
    }

    @Operation(summary = "Удаляет рассылку по ID")
    @DeleteMapping("/mailing/{id}")
    public ResponseEntity<String> deleteMailingById(@PathVariable Long id) {
        mailingService.deleteMailingById(id);
        return new ResponseEntity<>("Mailing with id= " + id + " was deleted", HttpStatus.OK);
    }
}
