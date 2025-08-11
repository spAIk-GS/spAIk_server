package com.spaik.backend.analysis.repository;

import com.spaik.backend.analysis.domain.FinalFeedback;
import com.spaik.backend.analysis.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FinalFeedbackRepository extends JpaRepository<FinalFeedback, Long> {
    Optional<FinalFeedback> findByReport(Report report);
}
