// 음성 피드백 데이터를 위한 리포지토리
package com.spaik.backend.analysis.repository;

import com.spaik.backend.analysis.domain.AudioFeedback;
import com.spaik.backend.analysis.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AudioFeedbackRepository extends JpaRepository<AudioFeedback, Long> {
    Optional<AudioFeedback> findByAnalysisId(String analysisId);
    Optional<AudioFeedback> findByReport(Report report);
}

