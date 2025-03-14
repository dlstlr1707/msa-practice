package com.example.msaboardproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsaBoardProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsaBoardProjectApplication.class, args);
    }

}
