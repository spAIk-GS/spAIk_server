package com.spaik.backend.analysis.s3;

public class PresignRequestDto {
    private String fileName;

    public PresignRequestDto() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}