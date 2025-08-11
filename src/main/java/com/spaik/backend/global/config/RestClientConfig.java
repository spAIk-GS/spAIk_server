package com.spaik.backend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient geminiRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl("https://generativelanguage.googleapis.com/")
                .build();
    }
}