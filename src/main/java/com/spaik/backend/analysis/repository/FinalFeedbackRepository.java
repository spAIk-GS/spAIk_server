// 최종 요약 피드백을 위한 리포지토리

package com.spaik.backend.analysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spaik.backend.report.entity.FinalFeedback;
import com.spaik.backend.report.entity.Report;

import java.util.Optional;

@Repository
public interface FinalFeedbackRepository extends JpaRepository<FinalFeedback, Long> {
    Optional<FinalFeedback> findByReport(Report report);
}

