// 영상 피드백 데이터를 위한 리포지토리

package com.spaik.backend.analysis.repository;

import com.spaik.backend.analysis.domain.VideoFeedback;
import com.spaik.backend.analysis.domain.Report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VideoFeedbackRepository extends JpaRepository<VideoFeedback, Long> {
    Optional<VideoFeedback> findByAnalysisId(String analysisId);
    Optional<VideoFeedback> findByReport(Report report);
}
