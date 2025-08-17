package com.spaik.backend.history;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportHistoryDto {
    private Long reportId;
    private String presentationId;
    private LocalDateTime createdAt;
}
