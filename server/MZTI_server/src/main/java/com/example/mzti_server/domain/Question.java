package com.example.mzti_server.domain;


import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@RequiredArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 5000) private String questionContent;

    private String questionType;

    @Builder
    public Question(String questionContent,String questionType) {
        this.questionContent = questionContent;
        this.questionType = questionType;
    }
}
