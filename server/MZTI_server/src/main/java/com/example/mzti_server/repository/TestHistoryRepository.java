package com.example.mzti_server.repository;

import com.example.mzti_server.domain.TestHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestHistoryRepository extends JpaRepository<TestHistory, Long> {
    List<TestHistory> findByMemberId(Long memberId);
    Optional<TestHistory> findTopByMbtiAndMemberIdOrderByCreatedDateDesc(String mbti, Long memberId);
}
