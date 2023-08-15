package com.example.mzti_server.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MbtiInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mbti;

    private String description;
}
