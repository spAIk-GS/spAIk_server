package com.spaik.backend.presentation.ai;

import com.spaik.backend.presentation.ai.dto.AiRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.spaik.backend.presentation.ai.exception.AiException;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class AiClient {

    private final RestTemplate restTemplate;

    @Value("${ai.server.url}")
    private String AI_SERVER_URL;

    public Map<String, Object> requestAnalysis(AiRequestDto requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AiRequestDto> requestEntity = new HttpEntity<>(requestDto, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                AI_SERVER_URL,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );


            return response.getBody();
        } catch (Exception e) {
            log.error("AI 서버 요청 실패", e);
            throw new AiException("AI 서버 분석 요청 실패", e);
        }
    }
}
