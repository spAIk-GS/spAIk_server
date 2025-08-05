package com.spaik.backend.analysis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalysisRequestDto {
    private String presentationId;
    private String s3Url;
}
