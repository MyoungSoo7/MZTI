package com.example.mzti_server.domain;

import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Map;

@Entity
@RequiredArgsConstructor
public class TestHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Member member;

}
