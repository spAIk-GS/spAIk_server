// 보고서 조회 응답을 위한 DTO 클래스

package com.spaik.backend.presentation.dto;

import com.spaik.backend.presentation.domain.FinalFeedback;
import com.spaik.backend.presentation.domain.Report;
import com.spaik.backend.presentation.domain.VideoFeedback;
import com.spaik.backend.presentation.domain.VoiceFeedback;
import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ReportResponse {
    private final Report report;
    private final List<VideoFeedback> videoFeedbackList;
    private final List<VoiceFeedback> voiceFeedbackList;
    private final FinalFeedback finalFeedback;
}
