package com.example.mzti_server.service;

import com.example.mzti_server.repository.MbtiDescriptionRepository;
import com.example.mzti_server.repository.MbtiInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
@RequiredArgsConstructor
public class InfoService {
    private final MbtiInfoRepository mbtiInfoRepository;
    private final MbtiDescriptionRepository mbtiDescriptionRepository;

    // MBTI 1개 조회
    public ResponseEntity<LinkedHashMap<String, Object>> getMBTIInfo(String mbti) {
        return null;
    }

    // MBTI 2개 비교
    public ResponseEntity<LinkedHashMap<String, Object>> getComparedMBTI(String leftMBTI, String rightMBTI) {
        return null;
    }
}
