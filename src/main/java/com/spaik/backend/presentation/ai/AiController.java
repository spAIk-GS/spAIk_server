package com.spaik.backend.presentation.ai;

import com.spaik.backend.presentation.ai.dto.AiRequestDto;
import com.spaik.backend.presentation.ai.dto.AiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/analyze")
    public ResponseEntity<AiResponseDto> analyze(@RequestBody AiRequestDto request) {
        AiResponseDto response = aiService.analyzeVideo(request);
        return ResponseEntity.ok(response);
    }
}
