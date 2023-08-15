package com.example.mzti_server.domain;

import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Map;

@Entity
@RequiredArgsConstructor
public class TestHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "questionId")
    private Question question;

    @OneToOne
    private Member member;

}
