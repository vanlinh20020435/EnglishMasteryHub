package com.emh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;


@Entity
@Getter
@Setter
public class StudentTestDetail {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "longtext")
    private String answerA;

    @Column(columnDefinition = "longtext")
    private String answerB;

    @Column(columnDefinition = "longtext")
    private String answerC;

    @Column(columnDefinition = "longtext")
    private String answerD;

    @Column(columnDefinition = "longtext")
    private String studentAnswer;

    @Column(nullable = false)
    private OffsetDateTime timest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_code_id", nullable = false)
    private Tests testCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Questions question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

}
