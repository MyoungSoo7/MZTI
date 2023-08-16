package com.example.mzti_server.dto.MBTI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MbtiDataDTO {
    private String key;
    private String[] content;
}