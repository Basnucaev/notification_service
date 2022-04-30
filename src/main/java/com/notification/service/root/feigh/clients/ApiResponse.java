package com.notification.service.root.feigh.clients;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ApiResponse {
    private Integer code;
    private HttpStatus message;
}
