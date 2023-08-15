package com.example.mzti_server.repository;

import com.example.mzti_server.domain.MbtiInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MbtiInfoRepository extends JpaRepository<MbtiInfo, Long> {
}
