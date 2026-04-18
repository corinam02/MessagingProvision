package com.example.messageprovisioning.provisioning.model;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MessageProvisioningApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageProvisioningApplication.class, args);
    }

}
