package com.spaik.backend.analysis.gemini;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GeminiClient {

    private final RestClient geminiRestClient;
    
    // final 필드가 아니므로 @RequiredArgsConstructor 생성자에서 제외됩니다.
    @Value("${gemini.api.key}")
    private String geminiApiKey;
    

    public String requestFinalFeedback(Long presentationId, String audioAnalysis, String videoAnalysis) {
        
        // 프롬프트 텍스트를 구성
        String prompt = String.format(
            "당신은 사용자의 면접 답변이나 발표를 피드백해주는 전문가입니다. 장점을 먼저 간략히 알려주고 내용의 논리성, 명확성, 구성, 그리고 청중에게 좋은 인상을 줄 수 있는 방법에 대해 간략하고 건설적인 피드백을 제공해주세요. " +
            "Audio analysis: %s. Video analysis: %s.",
            audioAnalysis,
            videoAnalysis
        );
        
        // Gemini API의 요청 본문 형식에 맞춰 Map을 구성
        Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                Map.of(
                    "parts", List.of(
                        Map.of("text", prompt)
                    )
                )
            )
        );

        // API 호출 및 응답 받기
        return geminiRestClient.post()
            // URI에 API 키를 쿼리 파라미터로 추가
            .uri(uriBuilder -> uriBuilder
                .path("/v1beta/models/gemini-1.5-pro-latest:generateContent")
                .queryParam("key", geminiApiKey)
                .build())
            .body(requestBody)
            .retrieve()
            .body(String.class); 
    }
}