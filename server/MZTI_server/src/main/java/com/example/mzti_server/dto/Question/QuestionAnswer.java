package com.example.mzti_server.dto.Question;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class QuestionAnswer {
    private String question; // 요청한 질문
    private String questionType;
    private String answer; // 요청한 mbti에 대한 정답들 -> 여기서 하나를 보여주면 될듯?
    private List<String> wronganswers; // 요청한 mbti에 대한 오답들 -> 이걸 나머지 보기로 사용하면 될듯!

    @Builder
    public QuestionAnswer(String question, String questionType, String answer, List<String> wronganswers){
        this.question = question;
        this.questionType =questionType;
        this.answer = answer;
        this.wronganswers = wronganswers;
    }
}
