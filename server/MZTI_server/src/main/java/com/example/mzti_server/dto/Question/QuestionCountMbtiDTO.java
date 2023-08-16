package com.example.mzti_server.dto.Question;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QuestionCountMbtiDTO {
    @NotNull private int questionCount;
    @NotNull private String mbti;
}
