package com.example.mzti_server.controller;

import com.example.mzti_server.dto.Question.QuestionAnswerResponseDTO;
import com.example.mzti_server.dto.Question.QuestionDTO;
import com.example.mzti_server.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "MBTI 문제", description = "MBTI 문제 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    @Operation(summary = "아이디 중복 체크", description = "아이디 중복 여부를 체크합니다.")
    @GetMapping("/search")
    public QuestionDTO getQuestion(@RequestParam Long q_id){
        return questionService.getQuestion(q_id);
    }

    @Operation(summary = "아이디 중복 체크", description = "아이디 중복 여부를 체크합니다.")
    @GetMapping()
    public QuestionAnswerResponseDTO getQuestionAnswers(@RequestParam int questionCount, @RequestParam String mbti){
        return questionService.getQuestionAnswers(questionCount, mbti);
    }
}
