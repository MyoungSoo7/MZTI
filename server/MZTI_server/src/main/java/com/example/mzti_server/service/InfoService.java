package com.example.mzti_server.service;

import com.example.mzti_server.domain.MbtiDescription;
import com.example.mzti_server.domain.MbtiInfo;
import com.example.mzti_server.dto.MBTI.MbtiDataDTO;
import com.example.mzti_server.dto.MBTI.MbtiInfoDTO;
import com.example.mzti_server.dto.MBTI.MbtiResponseDTO;
import com.example.mzti_server.repository.MbtiDescriptionRepository;
import com.example.mzti_server.repository.MbtiInfoRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class InfoService {
    private final MbtiInfoRepository mbtiInfoRepository;
    private final MbtiDescriptionRepository mbtiDescriptionRepository;

    // MBTI 1개 조회
    public ResponseEntity<LinkedHashMap<String, Object>> getMBTIInfo(String mbti) {
        MbtiInfoDTO mbtiInfoDTO = getMbtiInfoDTO(mbti);
        return getResponse(mbtiInfoDTO);
    }

    // MBTI 2개 비교
    public ResponseEntity<LinkedHashMap<String, Object>> getComparedMBTI(String leftMBTI, String rightMBTI) {
        List<MbtiInfoDTO> responseDTOList = new ArrayList<>();
        responseDTOList.add(getMbtiInfoDTO(leftMBTI));
        responseDTOList.add(getMbtiInfoDTO(rightMBTI));
        return getResponse(responseDTOList);
    }


    private MbtiInfoDTO getMbtiInfoDTO(String mbti) {
        MbtiInfo mbtiInfo = mbtiInfoRepository.findByCategory(mbti);

        List<MbtiDataDTO> temp = new ArrayList<>();

        List<MbtiDescription> mbtiDescriptionList = mbtiDescriptionRepository.findByMbtiInfoId(mbtiInfo.getId());
        String[] descriptions = new String[6];
        for (int i = 0; i < mbtiDescriptionList.size(); i++) {
            descriptions[i] = mbtiDescriptionList.get(i).getContent();
        }
        MbtiDataDTO descriptions1 = new MbtiDataDTO("descriptions", descriptions);
        temp.add(descriptions1);

        String goodMBTIs = mbtiInfo.getGoodMBTI();
        String badMBTIs = mbtiInfo.getBadMBTI();
        String goodPeoples = mbtiInfo.getGoodPeople();
        String goodJobs = mbtiInfo.getGoodJob();
        String keywords = mbtiInfo.getKeyword();
        String loveStyles = mbtiInfo.getLoveStyle();
        String talkingHabbits = mbtiInfo.getTalkingHabit();
        String virtualPeoples = mbtiInfo.getVirtualPeople();

        String[] keys = {"keyword", "goodJob", "loveStyle", "goodPeople", "goodMBTIs", "badMBTIs", "talkingHabbit", "virtualPeople"};
        String[] contents = {keywords, goodJobs, loveStyles, goodPeoples, goodMBTIs, badMBTIs, talkingHabbits, virtualPeoples};

        for (int i = 0; i < keys.length; i++) {
            String[] values = contents[i].split(",");
            MbtiDataDTO mbtiDataDTO = new MbtiDataDTO(keys[i], values);
            temp.add(mbtiDataDTO);
        }

        MbtiInfoDTO mbtiInfoDTO = new MbtiInfoDTO(temp);
        return mbtiInfoDTO;
    }


    private static ResponseEntity<LinkedHashMap<String, Object>> getResponse(Object saveMember) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("result_code", "200");
        response.put("result_data", saveMember);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
