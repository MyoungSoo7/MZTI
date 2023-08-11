package com.example.mzti_server.domain;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@RequiredArgsConstructor
public class FriendRelationship {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    private String username;
    private String profileImage;
    private String mbti;

    @Builder
    public FriendRelationship(Member member, String username, String profileImage, String mbti) {
        this.member = member;
        this.username = username;
        this.profileImage = profileImage;
        this.mbti = mbti;
    }
}
