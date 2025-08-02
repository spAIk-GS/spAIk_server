package com.spaik.backend.presentation.ai;

import com.spaik.backend.presentation.ai.dto.AiRequestDto;
import com.spaik.backend.presentation.ai.dto.AiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    private final AiClient aiClient;

    public AiResponseDto analyzeVideo(AiRequestDto requestDto) {
        Map<String, Object> result = aiClient.requestAnalysis(requestDto);

        // Map 결과에서 AiResponseDto 필드에 맞게 값 꺼내기
        String startTime = (String) result.getOrDefault("startTime", "");
        String endTime = (String) result.getOrDefault("endTime", "");
        String speedFeedback = (String) result.getOrDefault("speedFeedback", "");

        // AiResponseDto 생성자에 맞게 인자 전달
        return new AiResponseDto(startTime, endTime, speedFeedback);
    }

}
