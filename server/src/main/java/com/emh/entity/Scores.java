package com.emh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;


@Entity
@Getter
@Setter
public class Scores {

    @Id
    @Column(nullable = false, updatable = false, length = 10)
    private String scoreNumber;

    @Column(nullable = false, length = 50)
    private String scoreDetail;

    @Column
    private OffsetDateTime completionTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_code_id", nullable = false)
    private Tests testCode;

}
