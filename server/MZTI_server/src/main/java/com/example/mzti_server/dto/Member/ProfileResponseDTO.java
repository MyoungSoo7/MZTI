package com.example.mzti_server.dto.Member;

import com.example.mzti_server.dto.MBTIS;
import com.example.mzti_server.dto.Question.TestResultDTO;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.*;

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
    private List<TestResultDTO> testResult;

    @Builder
    public ProfileResponseDTO(String loginId, String username, String profileImage, String mbti, List<TestResultDTO> testResult) {
        this.loginId = loginId;
        this.username = username;
        this.profileImage = profileImage;
        this.mbti = mbti;
        this.testResult = testResult;
    }
}
