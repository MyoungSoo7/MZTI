package com.example.mzti_server.service;

import com.example.mzti_server.config.JwtTokenProvider;
import com.example.mzti_server.domain.Answer;
import com.example.mzti_server.domain.Member;
import com.example.mzti_server.domain.Question;
import com.example.mzti_server.domain.TestHistory;
import com.example.mzti_server.dto.Question.*;
import com.example.mzti_server.repository.AnswerRepository;
import com.example.mzti_server.repository.MemberRepository;
import com.example.mzti_server.repository.QuestionRepository;
import com.example.mzti_server.repository.TestHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    public final int QUESTION_COUNT = 16; // 문제 개수

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final TestHistoryRepository testHistoryRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private static ResponseEntity<LinkedHashMap<String, Object>> getResponse(Object saveMember) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("result_code", "200");
        response.put("result_data", saveMember);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<LinkedHashMap<String, Object>> getQuestion(Long qId) {
        Question question = questionRepository.findById(qId).get();
        List<Answer> answers = answerRepository.findAllByQuestionId(qId);
        List<String> answersList = answers.stream()
                .map(answer -> answer.getAnswerContent())
                .collect(Collectors.toList());
        return getResponse(QuestionDTO.builder()
                .questionContent(question.getQuestionContent())
                .answer(answersList)
                .build());
    }

    public ResponseEntity<LinkedHashMap<String, Object>> getQuestionAnswers(int questionCount, String mbti) {
        List<Question> randomQuestions = questionRepository.findRandomQuestions(questionCount);
        List<QuestionAnswer> qa = new ArrayList<>();
        for (int i = 0; i < questionCount; i++) {
            Answer randomAnswer = answerRepository.findRandomAnswer(randomQuestions.get(i).getId(), mbti);
            List<Answer> wrongAnswers = answerRepository.findRandomWrongAnswers(randomQuestions.get(i).getId(), mbti);
            List<String> wrongAnswerDTO = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                wrongAnswerDTO.add(wrongAnswers.get(j).getAnswerContent());
            }
            qa.add(QuestionAnswer.builder()
                    .question(randomQuestions.get(i).getQuestionContent())
                    .questionType(randomQuestions.get(i).getQuestionType())
                    .answer(randomAnswer.getAnswerContent())
                    .wronganswers(wrongAnswerDTO)
                    .build());
        }
        QuestionAnswerResponseDTO questionAnswerResponseDTO = new QuestionAnswerResponseDTO(mbti, qa);
        return getResponse(questionAnswerResponseDTO);
    }

    public Member memberByToken(String accessToken) {
        Optional<Member> member = memberRepository.findById(jwtTokenProvider.getMemberId(accessToken));
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new RuntimeException("토큰에 해당하는 멤버가 없습니다.");
        }
    }

    public ResponseEntity<LinkedHashMap<String, Object>> getResults(String accessToken, ResultRequestDTO resultRequestDTO) {
        Member testedMember = memberByToken(accessToken);
        System.out.println("testedMember = " + testedMember);
        String testedMbti = resultRequestDTO.getMbti();
        List<ResultRequestDTO.AnswerDTO> answerList = resultRequestDTO.getAnswerList();
        int[] resultTemp = {-1, -1, -1, -1};
        int correctCount = 0;
        for (int i = 0; i < answerList.size(); i++) {
            int typeNumber = answerList.get(i).getType();
            if (typeNumber == 0) {
                if (answerList.get(i).isCorrectFlag()) { // 맞춘경우
                    resultTemp[0] += 1;
                    correctCount += 1;
                }
            } else if (typeNumber == 1) {
                if (answerList.get(i).isCorrectFlag()) { // 맞춘경우
                    resultTemp[1] += 1;
                    correctCount += 1;
                }
            } else if (typeNumber == 2) {
                if (answerList.get(i).isCorrectFlag()) { // 맞춘경우
                    resultTemp[2] += 1;
                    correctCount += 1;
                }
            } else if (typeNumber == 3) {
                if (answerList.get(i).isCorrectFlag()) { // 맞춘경우
                    resultTemp[3] += 1;
                    correctCount += 1;
                }
            }
        }
        for (int k = 0; k < 4; k++) {
            if (resultTemp[k] == -1) {
                resultTemp[k] = 0;
            }
        }
        int score = Math.round((100 * correctCount) / QUESTION_COUNT);
        String mbtiResult = String.join("",
                String.valueOf(resultTemp[0]),
                String.valueOf(resultTemp[1]),
                String.valueOf(resultTemp[2]),
                String.valueOf(resultTemp[3])
        );
        TestHistory testHistory = TestHistory.builder()
                .member(testedMember)
                .mbti(testedMbti)
                .score(score)
                .mbtiResult(mbtiResult)
                .build();
        TestHistory save = testHistoryRepository.save(testHistory);
        System.out.println("save = " + save);
        ResultResponseDTO responseDTO = new ResultResponseDTO(mbtiResult, score);
        return getResponse(responseDTO);
    }

}
