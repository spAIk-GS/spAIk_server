package com.spaik.backend.analysis.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalysisRequestDto {
    private String presentationId;
    private String s3Url;
}
