package com.spaik.backend.history;

import com.spaik.backend.history.ReportHistoryDto;
import com.spaik.backend.history.ReportService;
import com.spaik.backend.user.entity.User;
import com.spaik.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final UserRepository userRepository;

    // 사용자별 보고서 히스토리 조회 (페이징 지원)
    @GetMapping("/history")
    public Page<ReportHistoryDto> getUserReportHistory(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable
    ) {
        // 로그인된 사용자 이메일로 User 조회
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long userId = user.getId();  // User ID 안전하게 가져오기
        return reportService.getReportsByUserId(userId, pageable);
    }
}
