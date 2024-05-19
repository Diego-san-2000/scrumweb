package com.tfg.scrumweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ScrumwebApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScrumwebApplication.class, args);
    }
}
