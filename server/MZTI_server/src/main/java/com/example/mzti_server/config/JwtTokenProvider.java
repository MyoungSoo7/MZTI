package com.example.mzti_server.config;


import com.example.mzti_server.domain.Member;
import com.example.mzti_server.dto.token.TokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;

@Component
public class JwtTokenProvider {

    private final Key key;

    @Value("${jwt.secret}")
    String salt;

    public JwtTokenProvider(@Value("${jwt.secret}") String salt) {
        this.key = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }

    public TokenInfo generateToken(Member member) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(member.getId()));
        claims.put("memberLoginId", member.getLoginId());

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .build();
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        System.out.println("accessToken = " + accessToken);
        Claims claims = parseClaims(accessToken);

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", Collections.emptyList());
        return new UsernamePasswordAuthenticationToken(principal, "", Collections.emptyList());
    }

    // JWT 토큰을 복호화하여 토큰의 유저 정보 꺼재는 메서드
    public Long getMemberId(String accessToken) {
        String token = getToken(accessToken);
        return Long.parseLong(Jwts.parserBuilder().setSigningKey(salt.getBytes()).build().parseClaimsJws(token).getBody().getSubject());
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (RuntimeException e) {
            System.out.println("e = " + e);
            throw new RuntimeException(e);
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (RuntimeException e) {
            System.out.println("ParseClaim 오류 = " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    // Bearer 제외부분
    public String getToken(String token) {
        if (token.length() < 7) {
            throw new RuntimeException("토큰에 오류가 있습니다.");
        }
        token = token.substring(7).trim();
        return token;
    }

}
