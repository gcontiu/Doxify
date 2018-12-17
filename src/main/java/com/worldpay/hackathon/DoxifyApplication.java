package com.worldpay.hackathon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class DoxifyApplication {
    public static void main(String[] args) {
        SpringApplication.run(DoxifyApplication.class, args);
    }
}
