package com.spaik.backend.presentation.s3;

public class PresignResponseDto {
    private String url;

    // 기본 생성자
    public PresignResponseDto() {
    }

    // 인자를 받는 생성자
    public PresignResponseDto(String url) {
        this.url = url;
    }

    // Getter
    public String getUrl() {
        return url;
    }

    // Setter (필요시, 선택사항)
    public void setUrl(String url) {
        this.url = url;
    }
}
