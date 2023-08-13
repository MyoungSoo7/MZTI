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

    @Builder
    public Question(String questionContent) {
        this.questionContent = questionContent;
    }
}
