package com.emh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Entity
@Getter
@Setter
public class StudentTestResult extends BaseEntity
{

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private Double score;

	@Column
	private Double testDefaultScore;

	@Column
	private Integer time;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "class_id", nullable = false)
	private Classes classs;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "test_code_id", nullable = false)
	private Tests tests;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;

	@OneToMany(mappedBy = "studentTestResult", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<QuestAnswerResult> questAnswerResults;
}
