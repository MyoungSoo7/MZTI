package com.example.mzti_server.domain;

import javax.persistence.*;

@Entity
public class MbtiDescription {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MbtiInfoId")
    private MbtiInfo mbtiInfo;

    @Column(length = 1000)
    private String content;
}
