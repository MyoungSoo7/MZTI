package com.example.mzti_server.domain;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Map;

@Entity
@RequiredArgsConstructor
public class TestHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    private String mbti;

    private int score;

    private String mbtiResult;

    @Builder
    public TestHistory(Member member, String mbti, int score, String mbtiResult) {
        this.member = member;
        this.mbti = mbti;
        this.score = score;
        this.mbtiResult = mbtiResult;
    }

}
