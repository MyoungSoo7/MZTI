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
public class Answer extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mbti;

    @Column(length = 5000) private String answerContent;

    @ManyToOne
    @JoinColumn(name = "questionId")
    private Question question;

    @Builder
    public Answer(String mbti, String answerContent, Question question) {
        this.mbti = mbti;
        this.answerContent = answerContent;
        this.question = question;
    }
}
