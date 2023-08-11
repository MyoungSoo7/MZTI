package com.example.mzti_server.repository;

import com.example.mzti_server.domain.FriendRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRelationshipRepository extends JpaRepository<FriendRelationship, Long> {
    Optional<List<FriendRelationship>> findByMemberId(Long id);
}
