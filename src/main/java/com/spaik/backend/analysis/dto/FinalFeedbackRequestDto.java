package com.spaik.backend.analysis.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinalFeedbackRequestDto {
    private Long presentationId;  // 평가할 발표 ID
}

