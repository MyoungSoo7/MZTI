package com.example.mzti_server.dto.MBTI;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
public class MbtiResponseDTO {
    private final String category;
    private final List<String> descriptions;
    private final String goodJob; // 어울리는 일
    private final String loveStyle; // 연애 스타일
    private final String goodPeople; // 잘 맞는 사람
    private final String goodMBTI; // 잘 맞는 MBTI
    private final String badMBTI; // 잘 안 맞는 MBTI
    private final String talkingHabit; // 말버릇
    private final String keyword; // 키워드
    private final String virtualPeople; // 가상 인물

    @Builder
    public MbtiResponseDTO(String category, List<String> descriptions, String goodJob, String loveStyle, String goodPeople, String goodMBTI, String badMBTI, String talkingHabit,
                           String keyword, String virtualPeople){
        this.category = category;
        this.descriptions = descriptions;
        this.goodJob = goodJob;
        this.loveStyle = loveStyle;
        this.goodPeople = goodPeople;
        this.goodMBTI = goodMBTI;
        this.badMBTI = badMBTI;
        this.talkingHabit = talkingHabit;
        this.keyword = keyword;
        this.virtualPeople = virtualPeople;
    }
}
