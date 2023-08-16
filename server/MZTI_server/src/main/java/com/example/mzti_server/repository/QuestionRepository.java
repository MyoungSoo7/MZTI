package com.example.mzti_server.repository;

import com.example.mzti_server.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    // @Query(value = "SELECT * FROM Question", nativeQuery = true)
    @Query(value = "SELECT * FROM Question ORDER BY RAND() LIMIT :questionCount", nativeQuery = true)
    List<Question> findRandomQuestions(int questionCount);
}
