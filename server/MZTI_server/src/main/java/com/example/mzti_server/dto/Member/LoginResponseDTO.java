package com.example.mzti_server.dto.Member;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginResponseDTO {

    private final String loginId;
    private final String generateType;
    private final String accessToken;

    public static class ResponseData {
        private final int result_code = 200;
        private final LoginResponseDTO result_data;

        public ResponseData(LoginResponseDTO result_data) {
            this.result_data = result_data;
        }

        public int getResult_code() {
            return result_code;
        }

        public LoginResponseDTO getResult_data() {
            return result_data;
        }
    }
}
