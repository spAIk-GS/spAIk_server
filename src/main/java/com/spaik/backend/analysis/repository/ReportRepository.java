package com.spaik.backend.analysis.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.spaik.backend.analysis.domain.Report;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByPresentationPresentationId(String presentationId);
    // User의 userId를 통해 Page 단위로 Report 조회
    Page<Report> findByPresentationUserId(Long userId, Pageable pageable);

}
