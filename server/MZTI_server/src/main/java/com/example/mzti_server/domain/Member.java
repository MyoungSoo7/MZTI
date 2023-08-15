package com.example.mzti_server.domain;


import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@RequiredArgsConstructor
public class Member extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;
    private String password;
    private String username;
    private String mbti;
    private String profileImage;

    @Builder
    public Member(String loginId, String password, String username, String mbti, String profileImage) {
        this.loginId = loginId;
        this.password = password;
        this.username = username;
        this.mbti = mbti;
        this.profileImage = profileImage; // 이미지 경로
    }

}
