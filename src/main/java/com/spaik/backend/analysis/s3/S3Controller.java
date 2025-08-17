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

    // 영상 업로드용 Presigned URL 생성
   @PostMapping("/videos/presign")
    public ResponseEntity<PresignResponseDto> getPresignedUrl(@RequestBody PresignRequestDto requestDto) {
        String url = s3Service.generatePresignedUrl(requestDto.getFileName());
        return ResponseEntity.ok(new PresignResponseDto(url));
        
    }

    // 영상 다운로드용 Presigned URL 생성
    @PostMapping("/videos/presign-get")
    public ResponseEntity<PresignResponseDto> getPresignedGetUrl(@RequestBody PresignRequestDto requestDto) {
        String url = s3Service.generatePresignedGetUrl(requestDto.getFileName());
        return ResponseEntity.ok(new PresignResponseDto(url));
    }

    // 썸네일 업로드용 Presigned URL 생성
    @PostMapping("/thumbnails/presign")
    public ResponseEntity<PresignResponseDto> getThumbnailPresignedUrl(@RequestBody PresignRequestDto requestDto) {
        String url = s3Service.generateThumbnailPresignedUrl(requestDto.getFileName());
        return ResponseEntity.ok(new PresignResponseDto(url));
    }

    // 썸네일 다운로드용 Presigned URL 생성
    @PostMapping("/thumbnails/presign-get")
    public ResponseEntity<PresignResponseDto> getThumbnailPresignedGetUrl(@RequestBody PresignRequestDto requestDto) {
        String url = s3Service.generateThumbnailPresignedGetUrl(requestDto.getFileName());
        return ResponseEntity.ok(new PresignResponseDto(url));
    }
}



