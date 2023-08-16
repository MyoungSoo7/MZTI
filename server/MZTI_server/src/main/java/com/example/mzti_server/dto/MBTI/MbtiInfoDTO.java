package com.example.mzti_server.dto.MBTI;

import com.example.mzti_server.service.InfoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MbtiInfoDTO {
    private List<MbtiDataDTO> data;
}
