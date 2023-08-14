package com.example.mzti_server.service;

import com.example.mzti_server.domain.Answer;
import com.example.mzti_server.domain.Question;
import com.example.mzti_server.dto.Question.QuestionAnswer;
import com.example.mzti_server.dto.Question.QuestionAnswerResponseDTO;
import com.example.mzti_server.dto.Question.QuestionDTO;
import com.example.mzti_server.repository.AnswerRepository;
import com.example.mzti_server.repository.QuestionRepository;
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

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

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
                .map(answer -> answer.getAnswerContent() + " (" + answer.getMbti() + ")")
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
                wrongAnswerDTO.add(wrongAnswers.get(j).getAnswerContent() + "(" + wrongAnswers.get(j).getMbti() + ")");
            }
            qa.add(QuestionAnswer.builder()
                    .question(randomQuestions.get(i).getQuestionContent())
                    .answer(randomAnswer.getAnswerContent())
                    .wronganswers(wrongAnswerDTO)
                    .build());
        }
        QuestionAnswerResponseDTO questionAnswerResponseDTO = new QuestionAnswerResponseDTO(mbti, qa);
        return getResponse(questionAnswerResponseDTO);
    }
}
