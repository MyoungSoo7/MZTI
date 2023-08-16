package com.example.mzti_server.controller;

import com.example.mzti_server.dto.Question.*;
import com.example.mzti_server.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

@Tag(name = "MBTI 문제", description = "MBTI 문제 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;

    @Operation(summary = "문제 검색", description = "문제ID를 통해 문제를 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<LinkedHashMap<String, Object>> getQuestion(@RequestParam Long questionId){
        return questionService.getQuestion(questionId);
    }

    @Operation(summary = "문제 모두 가져오기", description = "mbti와 문제 개수를 받으면 모든 문제들의 보기와 정답을 제공합니다.")
    @GetMapping()
    public ResponseEntity<LinkedHashMap<String, Object>> getQuestionAnswers(@RequestParam int questionCount, @RequestParam String mbti){
        // return questionService.getQuestionAnswers(questionCount, mbti);
        return questionService.getQuestionAnswers(2, "ESFJ");
    }

    @Operation(summary = "문제 결과 받기", description = "mbti와 문제 개수를 받으면 모든 문제들의 보기와 정답을 제공합니다.")
    @PostMapping("/result")
    public ResponseEntity<LinkedHashMap<String, Object>> getQuestionResult(HttpServletRequest request, @RequestBody ResultRequestDTO resultRequestDTO){
        return questionService.getResults(request.getHeader("Authorization"), resultRequestDTO);
    }
}
