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

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    /**
     * Gemini API 호출 - 최종 피드백 생성
     * @param audioJson Audio 분석 데이터(JSON 문자열)
     * @param videoJson Video 분석 데이터(JSON 문자열)
     * @return Gemini API 응답(JSON)
     */
    public String requestFinalFeedback(String audioJson, String videoJson) {
        // 🔹 프롬프트를 Gemini 내부에서 고정 관리
        String prompt = String.format("""
            당신은 사용자의 발표/면접을 분석하여 전문적인 피드백을 제공하는 발표 코치입니다.
            분석 데이터는 아래와 같습니다.

            [Audio 분석 데이터]
            %s

            [Video 분석 데이터]
            %s

            위 데이터를 종합 분석하여 다음을 작성하세요:
            1. 발표의 강점 (간결하고 구체적으로)
            2. 개선이 필요한 부분
            3. 실천 가능한 개선 팁 (행동/연습 방법 중심)
            4. 참고할 만한 자료나 훈련 방법 추천
            """, audioJson, videoJson);

        Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                Map.of(
                    "parts", List.of(
                        Map.of("text", prompt)
                    )
                )
            )
        );

        return geminiRestClient.post()
            .uri(uriBuilder -> uriBuilder
                .path("/v1beta/models/gemini-1.5-pro-latest:generateContent")
                .queryParam("key", geminiApiKey)
                .build())
            .body(requestBody)
            .retrieve()
            .body(String.class);
    }
}
