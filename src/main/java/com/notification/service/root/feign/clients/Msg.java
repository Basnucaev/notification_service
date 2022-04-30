package com.notification.service.root.feign.clients;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Msg {
    private Long id;
    private String phone;
    private String text;
}
