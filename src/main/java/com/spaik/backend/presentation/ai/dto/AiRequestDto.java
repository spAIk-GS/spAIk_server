package com.spaik.backend.presentation.ai.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiRequestDto {
    private String videoUrl;
    private String transcript;
    private String timeline;
    private String evaluator;
}
