// 음성 피드백 데이터를 위한 리포지토리

package com.spaik.backend.presentation.repository;

import com.spaik.backend.presentation.domain.Report;
import com.spaik.backend.presentation.domain.VoiceFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VoiceFeedbackRepository extends JpaRepository<VoiceFeedback, Long> {
    List<VoiceFeedback> findByReport(Report report);
}

