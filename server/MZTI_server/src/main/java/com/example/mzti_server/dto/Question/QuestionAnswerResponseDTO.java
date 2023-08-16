package com.example.mzti_server.dto.Question;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QuestionAnswerResponseDTO {

    private String mbti; // 요청한 mbti
    private List<QuestionAnswer> qa; // 질문과 정답 리스트
}
