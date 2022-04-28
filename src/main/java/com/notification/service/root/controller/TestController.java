package com.notification.service.root.controller;

import com.notification.service.root.feigh.clients.ApiResponse;
import com.notification.service.root.feigh.clients.Msg;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TestController {

    @PostMapping("/test/{id}")
    public ApiResponse sendMessage(@PathVariable Integer id, @RequestBody Msg msg) {
        return new ApiResponse(id, HttpStatus.OK);
    }
}
