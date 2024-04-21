package com.emh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Entity
@Getter
@Setter
public class Tests extends BaseEntity
{

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer testId;

	@Column(nullable = false)
	private String testName;

	@Column
	private Integer totalQuestions;

	@Column
	private Integer time;

	@Column(
			nullable = false,
			name = "\"description\"",
			columnDefinition = "longtext"
	)
	private String description;

	@Column(nullable = false)
	private Integer status;

	@Column
	private Boolean requiresGrading = false;

	@OneToMany(mappedBy = "tests", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private Set<Questions> questions;

	@OneToMany(mappedBy = "tests", cascade = CascadeType.REMOVE)
	private Set<StudentTestResult> testCodeStudentTestResults;

	@OneToMany(mappedBy = "tests", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private Set<TestClass> testClasses;

}
