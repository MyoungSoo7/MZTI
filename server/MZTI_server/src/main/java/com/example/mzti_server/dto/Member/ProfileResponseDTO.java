package com.example.mzti_server.dto.Member;

import com.example.mzti_server.dto.MBTIS;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class ProfileResponseDTO {

    @NotNull
    private final String loginId;
    @NotNull
    private final String username;
    @NotNull
    private final String profileImage;
    @NotNull
    private final String mbti;
    private LinkedHashMap<MBTIS, String> testResult;

    @Builder
    public ProfileResponseDTO(String loginId, String username, String profileImage, String mbti, LinkedHashMap testResult) {
        this.loginId = loginId;
        this.username = username;
        this.profileImage = profileImage;
        this.mbti = mbti;
        this.testResult = testResult;
    }
}
