package com.example.mzti_server.service;

import com.example.mzti_server.domain.MbtiDescription;
import com.example.mzti_server.domain.MbtiInfo;
import com.example.mzti_server.dto.MBTI.MbtiResponseDTO;
import com.example.mzti_server.repository.MbtiDescriptionRepository;
import com.example.mzti_server.repository.MbtiInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InfoService {
    private final MbtiInfoRepository mbtiInfoRepository;
    private final MbtiDescriptionRepository mbtiDescriptionRepository;

    // MBTI 1개 조회
    public ResponseEntity<LinkedHashMap<String, Object>> getMBTIInfo(String mbti) {
        MbtiResponseDTO mbtiResponseDTO = getMbtiResponseDTO(mbti);
        return getResponse(mbtiResponseDTO);
    }


    // MBTI 2개 비교
    public ResponseEntity<LinkedHashMap<String, Object>> getComparedMBTI(String leftMBTI, String rightMBTI) {
        List<MbtiResponseDTO> responseDTOList = new ArrayList<>();
        responseDTOList.add(getMbtiResponseDTO(leftMBTI));
        responseDTOList.add(getMbtiResponseDTO(rightMBTI));
        return getResponse(responseDTOList);
    }

    private MbtiResponseDTO getMbtiResponseDTO(String mbti) {
        MbtiInfo mbtiInfo = mbtiInfoRepository.findByCategory(mbti);
        List<MbtiDescription> mbtiDescriptionList = mbtiDescriptionRepository.findByMbtiInfoId(mbtiInfo.getId());
        List<String> descriptions = new ArrayList<>();
        for(int i=0;i<mbtiDescriptionList.size();i++){
            descriptions.add(mbtiDescriptionList.get(i).getContent());
        }
        MbtiResponseDTO mbtiResponseDTO = MbtiResponseDTO.builder().category(mbtiInfo.getCategory()).descriptions(descriptions).goodJob(mbtiInfo.getGoodJob())
                .loveStyle(mbtiInfo.getLoveStyle()).goodPeople(mbtiInfo.getGoodPeople()).goodMBTI(mbtiInfo.getGoodMBTI())
                .badMBTI(mbtiInfo.getBadMBTI()).talkingHabit(mbtiInfo.getTalkingHabit()).keyword(mbtiInfo.getKeyword()).virtualPeople(mbtiInfo.getVirtualPeople())
                .build();
        return mbtiResponseDTO;
    }

    private static ResponseEntity<LinkedHashMap<String, Object>> getResponse(Object saveMember) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("result_code", "200");
        response.put("result_data", saveMember);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
