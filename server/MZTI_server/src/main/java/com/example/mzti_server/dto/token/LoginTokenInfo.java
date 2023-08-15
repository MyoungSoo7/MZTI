package com.example.mzti_server.dto.token;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginTokenInfo {
    private final String loginId;
    private final String username;
    private final String mbti;
    private final String profileImage;
    private final String grantType;
    private final String accessToken;
}
