package com.example.mzti_server.service;

import com.example.mzti_server.config.JwtTokenProvider;
import com.example.mzti_server.domain.FriendRelationship;
import com.example.mzti_server.domain.Member;
import com.example.mzti_server.domain.TestHistory;
import com.example.mzti_server.dto.FriendListDTO;
import com.example.mzti_server.dto.MBTIS;
import com.example.mzti_server.dto.Member.EditMemberDTO;
import com.example.mzti_server.dto.Member.LoginResponseDTO;
import com.example.mzti_server.dto.Member.MemberDTO;
import com.example.mzti_server.dto.Member.ProfileResponseDTO;
import com.example.mzti_server.dto.token.LoginTokenInfo;
import com.example.mzti_server.dto.token.TokenInfo;
import com.example.mzti_server.repository.FriendRelationshipRepository;
import com.example.mzti_server.repository.MemberRepository;
import com.example.mzti_server.repository.TestHistoryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TestHistoryRepository testHistoryRepository;
    private final FriendRelationshipRepository friendRelationshipRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;

    public ResponseEntity<LinkedHashMap<String, Object>> login(String loginId, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(member.get());
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(loginId, tokenInfo.getGrantType(), tokenInfo.getAccessToken());

        return getResponse(loginResponseDTO);
    }

    public ResponseEntity<LinkedHashMap<String, Object>> signup(String loginId, String password, String username, String mbti) {
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
        Member saveMember = memberRepository.save(member);

        return getResponse(saveMember);

    }

    public ResponseEntity<LinkedHashMap<String, Object>> findMemberByLoginId(String loginId) {
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        if (member.isPresent()) {
            return getResponse(member.get());
        } else {
            throw new RuntimeException("아이디에 해당하는 멤버 정보가 없습니다.");
        }
    }

    public ResponseEntity<LinkedHashMap<String, Object>> editMember(String accessToken, EditMemberDTO editMemberDTO) {
        Member findByToken = memberByToken(accessToken);
        if (editMemberDTO.getUsername() == null) {
            editMemberDTO.setUsername(findByToken.getUsername());
        }
        if (editMemberDTO.getProfileImage() == null) {
            editMemberDTO.setProfileImage(findByToken.getProfileImage());
        }
        if (editMemberDTO.getMbti() == null) {
            editMemberDTO.setMbti(findByToken.getMbti());
        }
        findByToken.update(editMemberDTO);
        MemberDTO changedMember = MemberDTO.builder()
                .loginId(findByToken.getLoginId())
                .username(findByToken.getUsername())
                .profileImage(findByToken.getProfileImage())
                .mbti(findByToken.getMbti())
                .build();
        return getResponse(changedMember);
    }

    public ResponseEntity<LinkedHashMap<String, Object>> findFriendListByLoginId(String accessToken) {
        Optional<Member> findMember = memberRepository.findById(jwtTokenProvider.getMemberId(accessToken));
        if (findMember.isPresent()) {
            Optional<List<FriendRelationship>> friendRelationships = friendRelationshipRepository.findByMemberId(findMember.get().getId());
            if (friendRelationships.isPresent()) {
                List<FriendRelationship> friendRelationshipList = friendRelationships.get();
                List<MemberDTO> memberDTOS = new ArrayList<>();
                friendRelationshipList.stream().forEach(friendRelationship -> {
                    MemberDTO memberDTO = MemberDTO.builder()
                            .loginId(friendRelationship.getLoginId())
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
                return getResponse(friendListDTO);
            }
        } else { // 친구 관계가 없는 경우
            return getResponse(null);
        }
        throw new RuntimeException("토큰에 해당 멤버가 없습니다");
    }

    public ResponseEntity<LinkedHashMap<String, Object>> findMemberByToken(String accessToken) {
        Optional<Member> member = memberRepository.findById(jwtTokenProvider.getMemberId(accessToken));
        if (member.isPresent()) {
            LoginTokenInfo loginTokenInfo = new LoginTokenInfo(member.get().getLoginId(), member.get().getUsername(), member.get().getMbti(), member.get().getProfileImage(), "Bearer", accessToken);
            return getResponse(loginTokenInfo);
        } else {
            throw new RuntimeException("토큰에 해당하는 멤버가 없습니다.");
        }
    }

    public ResponseEntity<LinkedHashMap<String, Object>> addFriend(String accessToken, String friendId) {
        Member memberByToken = memberByToken(accessToken); // 본인
        Optional<Member> friend = memberRepository.findByLoginId(friendId); // 친구
        if (friend.isPresent()) {
            Optional<List<FriendRelationship>> byMemberId = friendRelationshipRepository.findByMemberId(memberByToken.getId());
            for(int i=0;i<byMemberId.get().size();i++){
                if(byMemberId.get().get(i).getUsername().equals(friend.get().getUsername())){
                    throw new RuntimeException("이미 추가된 친구입니다.");
                }
            }
            FriendRelationship friendRelationship = FriendRelationship.builder()
                    .member(memberByToken)
                    .loginId(friend.get().getLoginId())
                    .username(friend.get().getUsername())
                    .profileImage(friend.get().getProfileImage())
                    .mbti(friend.get().getMbti())
                    .build();
            friendRelationshipRepository.save(friendRelationship);
            MemberDTO responseFriend = MemberDTO.builder()
                    .loginId(friend.get().getLoginId())
                    .username(friend.get().getUsername())
                    .profileImage(friend.get().getProfileImage())
                    .mbti(friend.get().getMbti())
                    .build();
            return getResponse(responseFriend);
        } else {
            throw new RuntimeException("아이디에 해당하는 친구가 없습니다.");
        }
    }

    public ResponseEntity<LinkedHashMap<String, Object>> deleteFriend(String accessToken, String friendId) {
        Member memberByToken = memberByToken(accessToken); // 본인
        Optional<Member> friend = memberRepository.findByLoginId(friendId); // 친구
        if (friend.isPresent()) {
            FriendRelationship friendRelationship = FriendRelationship.builder()
                    .member(memberByToken)
                    .loginId(friend.get().getLoginId())
                    .username(friend.get().getUsername())
                    .profileImage(friend.get().getProfileImage())
                    .mbti(friend.get().getMbti())
                    .build();
            friendRelationshipRepository.delete(friendRelationship);
            return getResponse(memberByToken.getUsername() + "님이  " + friend.get().getUsername() + "님을 삭제하였습니다.");
        } else {
            throw new RuntimeException("아이디에 해당하는 친구가 없습니다.");
        }
    }

    public ResponseEntity<LinkedHashMap<String, Object>> checkId(String loginId) {
        Optional<Member> byLoginId = memberRepository.findByLoginId(loginId);
        if (byLoginId.isPresent()) {
            return getResponse("중복");
        } else {
            return getResponse("중복아님");
        }
    }

    public ResponseEntity<LinkedHashMap<String, Object>> getProfile(String accessToken) {
        Member findByToken = memberByToken(accessToken);
        // List<TestHistory> testHistories = testHistoryRepository.findByMemberId(findByToken.getId());
        LinkedHashMap<MBTIS, String> testResults = new LinkedHashMap<>();
        for (MBTIS mbti : MBTIS.values()) {
            System.out.println("mbti = " + mbti);
            Optional<TestHistory> testHistoryOptional = testHistoryRepository.findTopByMbtiAndMemberIdOrderByCreatedDateDesc(mbti.toString(), findByToken.getId());
            if (testHistoryOptional.isPresent()) {
                testResults.put(mbti, testHistoryOptional.get().getMbtiResult());
            } else {
                testResults.put(mbti, "");
            }
        }
        ProfileResponseDTO responseDTO = ProfileResponseDTO.builder()
                .loginId(findByToken.getLoginId())
                .username(findByToken.getUsername())
                .profileImage(findByToken.getProfileImage())
                .mbti(findByToken.getMbti())
                .testResult(testResults)
                .build();

        return getResponse(responseDTO);
    }

    public Member memberByToken(String accessToken) {
        Optional<Member> member = memberRepository.findById(jwtTokenProvider.getMemberId(accessToken));
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new RuntimeException("토큰에 해당하는 멤버가 없습니다.");
        }
    }

    private static ResponseEntity<LinkedHashMap<String, Object>> getResponse(Object saveMember) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("result_code", "200");
        response.put("result_data", saveMember);
        return new ResponseEntity<>(response, HttpStatus.OK);
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

