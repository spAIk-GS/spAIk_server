package com.spaik.backend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient geminiRestClient(RestClient.Builder builder) {

        // 타임아웃 설정을 위한 객체 생성
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(5));  // 서버에 연결을 시도하는 시간: 5초
        requestFactory.setReadTimeout(Duration.ofSeconds(60));   // 연결 후 응답을 기다리는 시간: 60초

        return builder
                .baseUrl("https://generativelanguage.googleapis.com/")
                .requestFactory(requestFactory) // 위에서 만든 타임아웃 설정을 적용
                .build();
    }
}