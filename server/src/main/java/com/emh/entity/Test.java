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

import java.time.OffsetDateTime;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Test
{

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
	private OffsetDateTime timest;

	@OneToMany(mappedBy = "testCode")
	private Set<QuestOfTest> testCodeQuestOfTests;

	@OneToMany(mappedBy = "testCode")
	private Set<Score> testCodeScores;

	@OneToMany(mappedBy = "testCode")
	private Set<StudentTestDetail> testCodeStudentTestDetails;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

}
