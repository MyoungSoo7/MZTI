package com.example.mzti_server.dto.Member;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginResponseDTO {

    private final String loginId;
    private final String generateType;
    private final String accessToken;
    private final String username;
    private final String mbti;
    private final String profileImage;
}
