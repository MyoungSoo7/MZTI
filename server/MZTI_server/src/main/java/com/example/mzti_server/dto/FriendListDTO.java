package com.example.mzti_server.dto;

import com.example.mzti_server.dto.Member.MemberDTO;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class FriendListDTO {
    private String username;
    private List<MemberDTO> friendlist = new ArrayList<>();

    @Builder
    public FriendListDTO(String username, List<MemberDTO> friendlist) {
        this.username = username;
        this.friendlist = friendlist;
    }
}
