package com.example.mzti_server.dto;

import lombok.Data;

@Data
public class ResponseDTO {
    private int result_code;
    private Object result_data;

    public ResponseDTO(int result_code, Object result_data) {
        this.result_code = result_code;
        this.result_data = result_data;
    }

    public int getResult_code() {
        return result_code;
    }

    public Object getResult_data() {
        return result_data;
    }
}
