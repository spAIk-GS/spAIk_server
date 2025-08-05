// 영상 피드백 데이터를 위한 리포지토리

package com.spaik.backend.analysis.repository;

import com.spaik.backend.analysis.domain.VideoFeedback;
import com.spaik.backend.report.entity.Report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VideoFeedbackRepository extends JpaRepository<VideoFeedback, Long> {
    List<VideoFeedback> findByReport(Report report);
}
