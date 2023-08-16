package com.example.mzti_server.repository;

import com.example.mzti_server.domain.MbtiDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MbtiDescriptionRepository extends JpaRepository<MbtiDescription, Long> {
    List<MbtiDescription> findByMbtiInfoId(Long mbtiInfoId);
}
