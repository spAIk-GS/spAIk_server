package com.spaik.backend.analysis.dto;
import com.spaik.backend.analysis.domain.AnalysisStatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinalFeedbackResponseDto {
    private Long finalFeedbackId;
    private Long reportId;
    private AnalysisStatus status;     
    private String finalFeedback;      // Gemini 최종평가 텍스트
}
