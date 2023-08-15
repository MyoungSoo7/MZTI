package com.example.mzti_server.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class MbtiDescription {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MbtiInfoId")
    private MbtiInfo mbtiInfo;

    @Column(length = 1000)
    private String content;
}
