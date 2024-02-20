package com.emh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Question
{

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer gradeId;

	@Column(nullable = false)
	private Integer unit;

	@Column(nullable = false, columnDefinition = "longtext")
	private String questionContent;

	@Column(nullable = false, columnDefinition = "longtext")
	private String answerA;

	@Column(nullable = false, columnDefinition = "longtext")
	private String answerB;

	@Column(nullable = false, columnDefinition = "longtext")
	private String answerC;

	@Column(nullable = false, columnDefinition = "longtext")
	private String answerD;

	@Column(nullable = false, columnDefinition = "longtext")
	private String correctAnswer;

	@Column(nullable = false)
	private Integer questionId;

	@Column(nullable = false)
	private Integer subjectId;

	@Column(nullable = false)
	private String sentBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "level_id", nullable = false)
	private Level level;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

	@OneToMany(mappedBy = "question")
	private Set<QuestOfTest> questionQuestOfTests;

	@OneToMany(mappedBy = "question")
	private Set<StudentTestDetail> questionStudentTestDetails;

}
