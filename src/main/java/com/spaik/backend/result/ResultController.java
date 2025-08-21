package com.spaik.backend.result; // 새로운 패키지를 만들거나 기존 패키지 활용

import com.spaik.backend.analysis.dto.FinalAnalysisResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/result")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;

    @GetMapping("/{presentationId}")
    public ResponseEntity<FinalAnalysisResultDto> getResultByPresentationId(
            @PathVariable String presentationId
    ) {
        FinalAnalysisResultDto result = resultService.getResultByPresentationId(presentationId);
        return ResponseEntity.ok(result);
    }
}