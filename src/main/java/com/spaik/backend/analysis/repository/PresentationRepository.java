package com.spaik.backend.analysis.repository;

import com.spaik.backend.analysis.domain.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PresentationRepository extends JpaRepository<Presentation, String> {
    // presentationId를 사용하여 Presentation 엔티티를 조회하는 메서드
    Optional<Presentation> findByPresentationId(String presentationId);
}