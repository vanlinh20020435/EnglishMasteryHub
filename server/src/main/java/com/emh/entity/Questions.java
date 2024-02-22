package com.emh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Entity
@Getter
@Setter
public class Questions {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questionId;

    @Column(nullable = false)
    private Integer unit;

    @Column(nullable = false)
    private Integer levelId;

    @Column(nullable = false, columnDefinition = "longtext")
    private String questionContent;

    @Column(nullable = false, columnDefinition = "longtext")
    private String correctAnswer;

    @Column(nullable = false)
    private String sentBy;

    @Column(nullable = false)
    private Integer status;

    @OneToMany(mappedBy = "question")
    private Set<QuestOfTest> questionQuestOfTests;

    @OneToMany(mappedBy = "question")
    private Set<StudentTestDetail> questionStudentTestDetails;

    @OneToMany(mappedBy = "question")
    private Set<QuestAnswer> questionQuestAnswers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private QuestType type;

}
