package com.example.mzti_server.service;

import com.amazonaws.services.s3.AmazonS3Client;
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
import com.example.mzti_server.dto.Question.TestResultDTO;
import com.example.mzti_server.dto.token.LoginTokenInfo;
import com.example.mzti_server.dto.token.TokenInfo;
import com.example.mzti_server.repository.FriendRelationshipRepository;
import com.example.mzti_server.repository.MemberRepository;
import com.example.mzti_server.repository.TestHistoryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final MemberRepository memberRepository;
    private final TestHistoryRepository testHistoryRepository;
    private final FriendRelationshipRepository friendRelationshipRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AmazonS3Client amazonS3Client;

    public ResponseEntity<LinkedHashMap<String, Object>> login(String loginId, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(member.get());
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(loginId, tokenInfo.getGrantType(), tokenInfo.getAccessToken(), member.get().getUsername(), member.get().getMbti(), member.get().getProfileImage());
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

    public ResponseEntity<LinkedHashMap<String, Object>> editMember(String accessToken, EditMemberDTO editMemberDTO, MultipartFile file) throws IOException {
        Member findByToken = memberByToken(accessToken);
        if (editMemberDTO == null) {
            if (file != null) {
                String fileName = file.getOriginalFilename();
                log.info("filesize",file.getSize());
                System.out.println("fileName = " + fileName);
                System.out.println();
                amazonS3Client.putObject(bucket, fileName, file.getInputStream(), null);
                String imageUrl = amazonS3Client.getUrl(bucket, fileName).toString();
                EditMemberDTO pi = new EditMemberDTO(findByToken.getUsername(), findByToken.getMbti(), imageUrl);
                findByToken.update(pi);
                memberRepository.save(findByToken);
                MemberDTO changedMember = MemberDTO.builder()
                        .loginId(findByToken.getLoginId())
                        .username(findByToken.getUsername())
                        .profileImage(findByToken.getProfileImage())
                        .mbti(findByToken.getMbti())
                        .build();
                return getResponse(changedMember);
            }
        } else {
            if (editMemberDTO.getUsername() == null) {
                editMemberDTO.setUsername(findByToken.getUsername());
            }
            if (editMemberDTO.getMbti() == null) {
                editMemberDTO.setMbti(findByToken.getMbti());
            }
            if (file != null) {
                String fileName = file.getOriginalFilename();
                amazonS3Client.putObject(bucket, fileName, file.getInputStream(), null);
                String imageUrl = amazonS3Client.getUrl(bucket, fileName).toString();
                editMemberDTO.setProfileImage(imageUrl);
            } else {
                editMemberDTO.setProfileImage(findByToken.getProfileImage());
            }
            findByToken.update(editMemberDTO);
            memberRepository.save(findByToken);
            MemberDTO changedMember = MemberDTO.builder()
                    .loginId(findByToken.getLoginId())
                    .username(findByToken.getUsername())
                    .profileImage(findByToken.getProfileImage())
                    .mbti(findByToken.getMbti())
                    .build();
            return getResponse(changedMember);
        }
        return null;
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
            if (friend.get().getId().equals(memberByToken.getId())) {
                throw new RuntimeException("본인과는 친구될 수 없습니다.");
            }
            Optional<List<FriendRelationship>> byMemberId = friendRelationshipRepository.findByMemberId(memberByToken.getId());
            for (int i = 0; i < byMemberId.get().size(); i++) {
                if (byMemberId.get().get(i).getUsername().equals(friend.get().getUsername())) {
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
            FriendRelationship byMemberIdAndLoginId = friendRelationshipRepository.findByMemberIdAndLoginId(memberByToken.getId(), friend.get().getLoginId());
            friendRelationshipRepository.delete(byMemberIdAndLoginId);
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
        List<TestResultDTO> testResultDTOList = new ArrayList<>();
        for (MBTIS mbti : MBTIS.values()) {
            System.out.println("mbti = " + mbti);
            Optional<TestHistory> testHistoryOptional = testHistoryRepository.findTopByMbtiAndMemberIdOrderByCreatedDateDesc(mbti.toString(), findByToken.getId());
            if (testHistoryOptional.isPresent()) {
                String mbtiResult = testHistoryOptional.get().getMbtiResult();
                int[] intArray = new int[mbtiResult.length()];

                for (int i = 0; i < mbtiResult.length(); i++) {
                    intArray[i] = Character.getNumericValue(mbtiResult.charAt(i));
                }
                TestResultDTO testResultDTO = TestResultDTO.builder()
                        .mbti(mbti.toString())
                        .size(intArray)
                        .isFlag(Boolean.TRUE)
                        .build();
                testResultDTOList.add(testResultDTO);
            } else {
                int[] intArray = new int[4];
                TestResultDTO testResultDTO = TestResultDTO.builder()
                        .mbti(mbti.toString())
                        .size(intArray)
                        .isFlag(Boolean.FALSE)
                        .build();
                testResultDTOList.add(testResultDTO);
            }
        }
        ProfileResponseDTO responseDTO = ProfileResponseDTO.builder()
                .loginId(findByToken.getLoginId())
                .username(findByToken.getUsername())
                .profileImage(findByToken.getProfileImage())
                .mbti(findByToken.getMbti())
                .testResult(testResultDTOList)
                .build();

        return getResponse(responseDTO);
    }

    public ResponseEntity<LinkedHashMap<String, Object>> updateProfileImage(String accessToken, MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            amazonS3Client.putObject(bucket, fileName, file.getInputStream(), null);
            String imageUrl = amazonS3Client.getUrl(bucket, fileName).toString();
            return getResponse(imageUrl);
        } catch (Exception e) {
            throw new RuntimeException("이미지 업로드 실패");
        }
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

