package com.example.mzti_server.dto.Question;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QuestionIdDTO {
    @NotNull private Long q_id;
}
