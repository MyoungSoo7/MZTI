package com.example.mzti_server.service;

import com.example.mzti_server.config.JwtTokenProvider;
import com.example.mzti_server.domain.FriendRelationship;
import com.example.mzti_server.domain.Member;
import com.example.mzti_server.dto.FriendListDTO;
import com.example.mzti_server.dto.Member.LoginResponseDTO;
import com.example.mzti_server.dto.Member.MemberDTO;
import com.example.mzti_server.dto.token.TokenInfo;
import com.example.mzti_server.repository.FriendRelationshipRepository;
import com.example.mzti_server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final FriendRelationshipRepository friendRelationshipRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;

    public LoginResponseDTO login(String loginId, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);

        System.out.println("authenticationToken = " + authenticationToken);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        System.out.println("authentication = " + authentication);
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(member.get());

        return new LoginResponseDTO(loginId, tokenInfo.getGrantType(), tokenInfo.getAccessToken());
    }

    public String signup(String loginId, String password, String username, String mbti) {
        final boolean isExistLoginId = memberRepository.existsByLoginId(loginId);
        if (isExistLoginId) {
            throw new RuntimeException("아이디 중복입니다");
        }
        Member member = Member.builder()
                .loginId(loginId)
                .password(passwordEncoder.encode(password))
                .username(username)
                .mbti(mbti)
                .build();
        memberRepository.save(member);
        return member.getUsername() + "님의 회원가입이 성공적으로 이루어졌습니다.";

    }

    public Member findMemberByLoginId(String loginId) {
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new RuntimeException("아이디에 해당하는 멤버 정보가 없습니다.");
        }
    }

    public FriendListDTO findFriendListByLoginId(String loginId) {
        Optional<Member> findMember = memberRepository.findByLoginId(loginId);
        if (findMember.isPresent()) {
            Optional<List<FriendRelationship>> friendRelationships = friendRelationshipRepository.findByMemberId(findMember.get().getId());
            if (friendRelationships.isPresent()) {
                List<FriendRelationship> friendRelationshipList = friendRelationships.get();
                List<MemberDTO> memberDTOS = new ArrayList<>();
                friendRelationshipList.stream().forEach(friendRelationship -> {
                    MemberDTO memberDTO = MemberDTO.builder()
                            .username(friendRelationship.getUsername())
                            .profileImage(friendRelationship.getProfileImage())
                            .mbti(friendRelationship.getMbti())
                            .build();
                    memberDTOS.add(memberDTO);
                });
                FriendListDTO friendListDTO = FriendListDTO.builder()
                        .username(findMember.get().getUsername())
                        .friendlist(memberDTOS)
                        .build();
                return friendListDTO;
            }
        } else { // 친구 관계가 없는 경우
            return null;
        }
        return null;
    }

    public Member findMemberByToken(String accessToken) {
        Optional<Member> member = memberRepository.findById(jwtTokenProvider.getMemberId(accessToken));
        if (member.isPresent()) {
            return member.get();
        }else{
            throw new RuntimeException("토큰에 해당하는 멤버가 없습니다.");
        }
    }
}

//    public String signup(String id, String password, String nickname, String mbti, MultipartFile profileImage) {
//        Member member = Member.builder()
//                .password(passwordEncoder.encode(password))
//                .build();
//        System.out.println("member = " + member);
//        Member savedMember = memberRepository.save(member);
//
//        return savedMember;
//    }

//    @Transactional(readOnly = false)
//    private MemberImage saveMemberImage(MultipartFile file) {
//        if(file.getContentType().startsWith("image") == false) {
//            log.warn("이미지 파일이 아닙니다.");
//            return null;
//        }
//
//        String originalName = file.getOriginalFilename();
//        Path root = Paths.get(uploadPath, "member");
//
//        try {
//            ImageDTO imageDTO =  fileService.createImageDTO(originalName, root);
//            MemberImage memberImage = MemberImage.builder()
//                    .uuid(imageDTO.getUuid())
//                    .fileName(imageDTO.getFileName())
//                    .fileUrl(imageDTO.getFileUrl())
//                    .build();
//
//            file.transferTo(Paths.get(imageDTO.getFileUrl()));
//
//            return imageRepository.save(memberImage);
//        } catch (IOException e) {
//            log.warn("업로드 폴더 생성 실패: " + e.getMessage());
//        }
//
//        return null;
//    }

