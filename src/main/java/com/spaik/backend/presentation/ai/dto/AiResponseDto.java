package com.spaik.backend.presentation.ai.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AiResponseDto {
    private String startTime;
    private String endTime;
    private String speedFeedback;
}

