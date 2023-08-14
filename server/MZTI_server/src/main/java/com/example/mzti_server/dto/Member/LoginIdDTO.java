package com.example.mzti_server.dto.Member;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginIdDTO {
    @NotNull
    private String loginId;
}
