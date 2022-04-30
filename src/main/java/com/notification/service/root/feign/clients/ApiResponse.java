package com.notification.service.root.feign.clients;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ApiResponse {
    private Integer code;
    private HttpStatus message;
}
