package com.example.mzti_server.repository;

import com.example.mzti_server.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByQuestionId(Long qId);
    List<Answer> findAllByQuestionIdAndMbti(Long questionId, String mbti); // mbti에 해당하는 모든 정답들 가져오기
    //Answer findTopByQuestionIdAndMbtiOrderByRand(Long questionId, String mbti); // mbti에 해당하는 정답 하나 가져오기
    // List<Answer> findTop2ByQuestionIdAndMbtiNotOrderByRand(Long questionId, String mbti); // mbti에 해당하지 않는 정답 2개 가져오기

    // questionId에 해당하면서 mbti가 주어진 값과 다른 2개의 랜덤한 Answer를 가져오는 메서드
    @Query(value = "SELECT * FROM Answer " +
            "WHERE questionId = :questionId " +
            "AND mbti <> :mbti " +
            "ORDER BY RAND() " +
            "LIMIT 2", nativeQuery = true)
    List<Answer> findRandomWrongAnswers(Long questionId, String mbti);

    // questionId에 해당하면서 mbti가 주어진 값과 일치하는 랜덤한 Answer를 가져오는 메서드
    @Query(value = "SELECT * FROM Answer " +
            "WHERE questionId = :questionId " +
            "AND mbti = :mbti " +
            "ORDER BY RAND() " +
            "LIMIT 1", nativeQuery = true)
    Answer findRandomAnswer(Long questionId, String mbti);
}
