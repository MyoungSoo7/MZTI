package com.example.mzti_server.dto.Question;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {

    private String questionContent;
    private List<String> answer;

    @Builder
    public QuestionDTO(String questionContent, List<String> answer) {
        this.questionContent = questionContent;
        this.answer = answer;
    }
}
