package com.example.mzti_server.dto.Question;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TestResultDTO {

    private String mbti;
    private int[] size;
    private Boolean isFlag;

    @Builder
    public TestResultDTO(String mbti, int[] size, Boolean isFlag) {
        this.mbti = mbti;
        this.size = size;
        this.isFlag = isFlag;
    }
}
