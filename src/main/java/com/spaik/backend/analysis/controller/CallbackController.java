/*
 
package com.spaik.backend.analysis.controller;

import com.spaik.backend.analysis.dto.VideoAnalysisResultDto;
import com.spaik.backend.analysis.dto.VoiceAnalysisResultDto;
import com.spaik.backend.analysis.service.AnalysisCallbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analysis/callback")
@RequiredArgsConstructor
public class CallbackController {

    private final CallbackService callbackService;

    @PostMapping("/video")
    public void receiveVideoAnalysisResult(@RequestBody VideoAnalysisResultDto dto) {
        callbackService.saveVideoResult(dto);
    }

    @PostMapping("/voice")
    public void receiveVoiceAnalysisResult(@RequestBody VoiceAnalysisResultDto dto) {
        callbackService.saveVoiceResult(dto);
    }
}

*/