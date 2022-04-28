package com.notification.service.root.feigh.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "messageSender", url = "https://probe.fbrq.cloud", path = "v1")
public interface SendMessagesClient {

    @PostMapping(value = "/send/{msgId}", headers = "Authorization=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODI1MzA4NTQsImlzcyI6ImZhYnJpcXVlIiwibmFtZSI6IkJhc251Y2FldjAifQ.WXuLP_mcP3jta1KRJsbUateilMv_SAd1Wbf8CZRJsGk")
    ApiResponse sendMessage(@PathVariable Integer msgId, @RequestBody Msg message);
}
