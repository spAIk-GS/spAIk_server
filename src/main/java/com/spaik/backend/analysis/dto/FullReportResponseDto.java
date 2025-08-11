package com.spaik.backend.analysis.dto;

import com.spaik.backend.analysis.domain.AudioFeedback;
import com.spaik.backend.analysis.domain.VideoFeedback;
import com.spaik.backend.analysis.domain.FinalFeedback;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FullReportResponseDto {
    private Long reportId;
    private Long presentationId;
    private AudioFeedback audioFeedback;
    private VideoFeedback videoFeedback;
    private FinalFeedback finalFeedback;
}