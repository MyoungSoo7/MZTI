package com.example.mzti_server.dto.Member;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class MemberDTO {

    private final String username;
    private final String profileImage;
    private final String mbti;

    @Builder
    public MemberDTO(String username, String profileImage, String mbti) {
        this.username = username;
        this.profileImage = profileImage;
        this.mbti = mbti;
    }
}
