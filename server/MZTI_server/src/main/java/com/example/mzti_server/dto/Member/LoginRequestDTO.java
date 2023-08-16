package com.example.mzti_server.dto.Member;

import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class LoginRequestDTO {
    @NotNull private String loginId;
    @NotNull private String password;
}
