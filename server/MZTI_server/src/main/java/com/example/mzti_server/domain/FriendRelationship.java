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
public class FriendRelationship extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    private String loginId;
    private String username;
    private String profileImage;
    private String mbti;

    @Builder
    public FriendRelationship(Member member, String loginId, String username, String profileImage, String mbti) {
        this.member = member;
        this.loginId = loginId;
        this.username = username;
        this.profileImage = profileImage;
        this.mbti = mbti;
    }
}
