package com.example.mzti_server.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ImageDTO {
    private final String fileName;
    private final String uuid;
    private final String fileUrl;

    @Builder
    public ImageDTO(String fileName, String uuid, String fileUrl) {
        this.fileName = fileName;
        this.uuid = uuid;
        this.fileUrl = fileUrl;
    }
}
