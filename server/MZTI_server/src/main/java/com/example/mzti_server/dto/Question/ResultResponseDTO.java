package com.example.mzti_server.dto.Question;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ResultResponseDTO {

    private final String mbtiResult; // "3201"
    private final int score;
}
