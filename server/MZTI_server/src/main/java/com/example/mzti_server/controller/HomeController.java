package com.example.mzti_server.controller;

import com.example.mzti_server.config.AmazonS3Config;
import com.example.mzti_server.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final FileUploadService fileUploadService;

    @GetMapping("/")
    public String home(){
        return "<h1>MZTI 어플리케이션 서버입니다</h1>";
    }

    private final AmazonS3Config amazonS3Config;
    @PostMapping("/upload")
    public ResponseEntity<ResponseEntity<String>> post(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        return ResponseEntity.ok(fileUploadService.save(multipartFile));
    }
}
