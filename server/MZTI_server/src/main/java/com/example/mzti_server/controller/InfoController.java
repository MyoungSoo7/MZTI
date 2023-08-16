package com.example.mzti_server.controller;

import com.example.mzti_server.service.InfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;

@Tag(name = "MBTI 정보", description = "MBTI 정보 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/info")
public class InfoController {

    private final InfoService infoService;

    @Operation(summary = "MBTI 정보", description = "mbti에 대한 정보를 제공합니다.")
    @GetMapping("")
    public ResponseEntity<LinkedHashMap<String, Object>> getMBTIInfo(@RequestParam String mbti){
        return infoService.getMBTIInfo(mbti);
    }

    @Operation(summary = "MBTI 비교", description = "2개의 mbti에 대한 비교 정보를 제공합니다.")
    @GetMapping("/compare")
    public ResponseEntity<LinkedHashMap<String, Object>> getComparedMBTI(@RequestParam String leftMBTI, @RequestParam String rightMBTI){
        return infoService.getComparedMBTI(leftMBTI, rightMBTI);
    }
}
