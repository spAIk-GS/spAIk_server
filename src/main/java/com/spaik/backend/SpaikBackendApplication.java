package com.spaik.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpaikBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpaikBackendApplication.class, args);
    }
}
