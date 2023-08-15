package com.example.mzti_server.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MbtiInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category; // mbti이름

    private String goodJob; // 어울리는 일

    private String loveStyle; // 연애 스타일

    private String goodPeople; // 잘 맞는 사람

    private String goodMBTI; // 잘 맞는 MBTI

    private String badMBTI; // 잘 안 맞는 MBTI

    private String talkingHabit; // 말버릇

    private String keyword; // 키워드

    private String virtualPeople; // 가상 인물
}
