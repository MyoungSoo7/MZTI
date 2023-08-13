package com.example.mzti_server.controller;

import com.example.mzti_server.dto.Question.QuestionAnswerResponseDTO;
import com.example.mzti_server.dto.Question.QuestionDTO;
import com.example.mzti_server.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/search")
    public QuestionDTO getQuestion(@RequestParam Long q_id){
        return questionService.getQuestion(q_id);
    }

    @GetMapping()
    public QuestionAnswerResponseDTO getQuestionAnswers(@RequestParam int questionCount, @RequestParam String mbti){
        return questionService.getQuestionAnswers(questionCount, mbti);
    }
}
