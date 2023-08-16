package com.example.mzti_server.dto.Member;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SignupRequestDTO {
    @NotNull private String loginId;
    @NotNull private String password;
    @NotNull private String username;
    @NotNull private String mbti;
}
