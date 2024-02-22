package com.emh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Set;


@Entity
@Getter
@Setter
public class Tests {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer testCode;

    @Column(nullable = false)
    private String testName;

    @Column(nullable = false, length = 32)
    private String password;

    @Column
    private Integer subjectId;

    @Column(nullable = false)
    private Integer gradeId;

    @Column(nullable = false)
    private Integer totalQuestions;

    @Column(nullable = false)
    private Integer timeToDo;

    @Column(nullable = false, columnDefinition = "longtext")
    private String note;

    @Column(nullable = false)
    private Integer status;

    @Column(nullable = false)
    private OffsetDateTime timest;

    @OneToMany(mappedBy = "testCode")
    private Set<QuestOfTest> testCodeQuestOfTests;

    @OneToMany(mappedBy = "testCode")
    private Set<Scores> testCodeScoreses;

    @OneToMany(mappedBy = "testCode")
    private Set<StudentTestDetail> testCodeStudentTestDetails;

}
