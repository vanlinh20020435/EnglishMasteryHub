package com.emh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class StudentTestDetail
{

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
	private Test testCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id", nullable = false)
	private Question question;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;

}
