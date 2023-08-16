package com.example.mzti_server.service;

import com.example.mzti_server.domain.Member;
import com.example.mzti_server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws RuntimeException {
        return memberRepository.findByLoginId(loginId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new RuntimeException("해당하는 유저를 찾을 수 없습니다."));
    }

    private UserDetails createUserDetails(Member member) {
        return User.builder()
                .username(member.getLoginId())
                .password(member.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }
}
