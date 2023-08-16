package com.example.mzti_server.dto.Member;

import lombok.Builder;
import lombok.Data;

@Data
public class EditMemberDTO {
    private String username;
    private String mbti;
    private String profileImage;

    @Builder
    public EditMemberDTO(String username, String mbti, String profileImage) {
        this.username = username;
        this.mbti = mbti;
        this.profileImage = profileImage;
    }
}
