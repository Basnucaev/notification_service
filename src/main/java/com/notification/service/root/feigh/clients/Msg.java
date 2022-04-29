package com.notification.service.root.feigh.clients;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Msg {
    private Long id;
    private String phone;
    private String text;
}
