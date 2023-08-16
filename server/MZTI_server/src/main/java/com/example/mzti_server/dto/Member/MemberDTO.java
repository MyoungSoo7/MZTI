package com.example.mzti_server.dto.Member;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
public class MemberDTO {

    @NotNull private final String loginId;
    @NotNull private final String username;
    @NotNull private final String profileImage;
    @NotNull private final String mbti;

    @Builder
    public MemberDTO(String loginId, String username, String profileImage, String mbti) {
        this.loginId = loginId;
        this.username = username;
        this.profileImage = profileImage;
        this.mbti = mbti;
    }
}
