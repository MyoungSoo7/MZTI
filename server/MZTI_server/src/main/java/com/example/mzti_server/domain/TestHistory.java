package com.example.mzti_server.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Map;

@EqualsAndHashCode(callSuper=false)
@Entity
@RequiredArgsConstructor
@Data
public class TestHistory extends BaseTime {

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
