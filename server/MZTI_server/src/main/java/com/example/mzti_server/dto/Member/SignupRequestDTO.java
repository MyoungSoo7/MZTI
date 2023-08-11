package com.example.mzti_server.dto.Member;

import lombok.Data;

@Data
public class SignupRequestDTO {
    private String loginId;
    private String password;
    private String username;
    private String mbti;
}
