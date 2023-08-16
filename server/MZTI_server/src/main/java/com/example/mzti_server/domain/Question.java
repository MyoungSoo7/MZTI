package com.example.mzti_server.domain;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper=false)
@Entity
@Data
@RequiredArgsConstructor
public class Question extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 5000) private String questionContent;

    private String questionType; // "EI", "NS", "TF", "PJ"

    @Builder
    public Question(String questionContent,String questionType) {
        this.questionContent = questionContent;
        this.questionType = questionType;
    }
}
