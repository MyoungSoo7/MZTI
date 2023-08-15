package com.example.mzti_server.dto.Question;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ResultRequestDTO {
    private String mbti;
    private List<AnswerDTO> answerList = new ArrayList<>();

    @Data
    public static class AnswerDTO {
        private int type;
        private boolean correctFlag;
    }
}
