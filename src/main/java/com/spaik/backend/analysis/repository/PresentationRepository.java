// 발표 영상 저장 및 조회를 위한 JPA 리포지토리

package com.spaik.backend.analysis.repository;

import com.spaik.backend.analysis.domain.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresentationRepository extends JpaRepository<Presentation, Long> {

}
