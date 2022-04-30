package com.notification.service.root;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@OpenAPIDefinition(info = @Info(title = "Notification Service", version = "1.0", description =
        "The service is needed in order to create mailings, add clients to the\n" +
                "    database and launch mailings with a message for suitable clients. Clients\n" +
                "    for mailing are selected by the code of the mobile operator. You can view\n" +
                "    the statistics of sent messages for each individual mailing list or for all\n" +
                "    mailings at once."))
public class NotificationServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}
