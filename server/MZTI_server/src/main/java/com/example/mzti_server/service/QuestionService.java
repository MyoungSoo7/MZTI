package com.example.mzti_server.service;

import com.example.mzti_server.domain.Answer;
import com.example.mzti_server.domain.Question;
import com.example.mzti_server.dto.Question.QuestionAnswer;
import com.example.mzti_server.dto.Question.QuestionAnswerResponseDTO;
import com.example.mzti_server.dto.Question.QuestionDTO;
import com.example.mzti_server.repository.AnswerRepository;
import com.example.mzti_server.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public QuestionDTO getQuestion(Long qId) {
        Question question = questionRepository.findById(qId).get();
        List<Answer> answers = answerRepository.findAllByQuestionId(qId);
        List<String> answersList = answers.stream()
                .map(answer -> answer.getAnswerContent() + " (" + answer.getMbti() + ")")
                .collect(Collectors.toList());
        return QuestionDTO.builder()
                .questionContent(question.getQuestionContent())
                .answer(answersList)
                .build();
    }

    public QuestionAnswerResponseDTO getQuestionAnswers(int questionCount, String mbti) {
        List<Question> randomQuestions = questionRepository.findRandomQuestions(questionCount);
        for(Question rq: randomQuestions){
            System.out.println(rq.getQuestionContent());
        }
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
        return questionAnswerResponseDTO;
    }
}
