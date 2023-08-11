package com.example.mzti_server.dto.Member;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String loginId;
    private String password;
}
