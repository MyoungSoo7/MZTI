package com.example.mzti_server.repository;

import com.example.mzti_server.domain.TestHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestHistoryRepository extends JpaRepository<TestHistory, Long> {
}
