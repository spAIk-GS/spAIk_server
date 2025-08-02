// 영상 피드백 데이터를 위한 리포지토리

package com.spaik.backend.presentation.repository;

import com.spaik.backend.presentation.domain.Report;
import com.spaik.backend.presentation.domain.VideoFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VideoFeedbackRepository extends JpaRepository<VideoFeedback, Long> {
    List<VideoFeedback> findByReport(Report report);
}
