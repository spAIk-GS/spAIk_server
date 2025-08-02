// 발표 영상을 업로드하고, AWS S3에 저장, Python AI 서버로 분석 요청
// 분석 결과를 DB에 저장하는 서비스 클래스

package com.spaik.backend.presentation.service;

import com.spaik.backend.presentation.domain.*;
import com.spaik.backend.presentation.dto.ReportResponse;
import com.spaik.backend.presentation.repository.*;
import com.spaik.backend.presentation.ai.AiService;
import com.spaik.backend.presentation.ai.dto.AiRequestDto;
import com.spaik.backend.presentation.ai.dto.AiResponseDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PresentationService {

    private final PresentationRepository presentationRepo;
    private final ReportRepository reportRepo;
    private final VoiceFeedbackRepository voiceFeedbackRepo;
    private final VideoFeedbackRepository videoFeedbackRepo;
    private final FinalFeedbackRepository finalFeedbackRepo;
    private final AiService aiService;

    // 클라이언트가 업로드 완료 후 호출하는 메서드
    public String registerUploadedPresentation(String s3ObjectKey, String originalFilename) {
        String bucketName = "your-bucket-name";  // 실제 버킷명으로 바꾸세요
        String s3Url = "https://" + bucketName + ".s3.amazonaws.com/" + s3ObjectKey;

        // Presentation 엔티티 저장
        Presentation presentation = Presentation.builder()
                .title(originalFilename)
                .s3VideoUrl(s3Url)
                .createdAt(LocalDateTime.now())
                .build();
        presentationRepo.save(presentation);

        // AI 분석 요청 DTO 생성
        AiRequestDto requestDto = AiRequestDto.builder()
                .videoUrl(s3Url)
                .build();

        // AI 분석 요청 및 결과 수신
        AiResponseDto response = aiService.analyzeVideo(requestDto);

        // Report 및 Feedback 저장
        Report report = Report.builder()
                .presentation(presentation)
                .createdAt(LocalDateTime.now())
                .build();
        reportRepo.save(report);

        VoiceFeedback voiceFeedback = VoiceFeedback.builder()
                .report(report)
                .startTime(response.getStartTime())
                .endTime(response.getEndTime())
                .type("speed")
                .value(response.getSpeedFeedback())
                .createdAt(LocalDateTime.now())
                .build();
        voiceFeedbackRepo.save(voiceFeedback);

        VideoFeedback videoFeedback = VideoFeedback.builder()
                .report(report)
                .bodyMovement(null)
                .gazeOut(null)
                .createdAt(LocalDateTime.now())
                .build();
        videoFeedbackRepo.save(videoFeedback);

        FinalFeedback finalFeedback = FinalFeedback.builder()
                .report(report)
                .finalFeedback("")
                .createdAt(LocalDateTime.now())
                .build();
        finalFeedbackRepo.save(finalFeedback);

        return s3Url;
    }

    // 리포트 조회 메서드 추가
    public ReportResponse getReport(Long presentationId) {
        Report report = reportRepo.findByPresentation_PresentationId(presentationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 발표에 대한 리포트를 찾을 수 없습니다."));

        List<VideoFeedback> videoFeedbackList = videoFeedbackRepo.findByReport(report);
        List<VoiceFeedback> voiceFeedbackList = voiceFeedbackRepo.findByReport(report);
        FinalFeedback finalFeedback = finalFeedbackRepo.findByReport(report).orElse(null);

        return ReportResponse.builder()
                .report(report)
                .videoFeedbackList(videoFeedbackList)
                .voiceFeedbackList(voiceFeedbackList)
                .finalFeedback(finalFeedback)
                .build();
    }
}
