// API 요청 수신, Presigned URL 반환
package com.spaik.backend.analysis.s3;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

import com.spaik.backend.analysis.s3.PresignRequestDto;
import com.spaik.backend.analysis.s3.PresignResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

   @PostMapping("/videos/presign")
    public ResponseEntity<PresignResponseDto> getPresignedUrl(@RequestBody PresignRequestDto requestDto) {
        String url = s3Service.generatePresignedUrl(requestDto.getFileName());
        return ResponseEntity.ok(new PresignResponseDto(url));
        
    }
}



