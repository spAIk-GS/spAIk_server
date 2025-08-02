// 분석 결과 보고서 엔티티를 위한 리포지토리

package com.spaik.backend.presentation.repository;

import com.spaik.backend.presentation.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByPresentation_PresentationId(Long presentationId);
}


