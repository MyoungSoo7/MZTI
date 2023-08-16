package com.example.mzti_server.dto.token;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Data
public class TokenInfo {

    private final String grantType;
    private final String accessToken;
}
