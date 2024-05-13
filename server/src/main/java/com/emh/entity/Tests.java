package com.emh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import java.util.Set;


@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE tests SET deleted = true WHERE id=?")
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

	private boolean deleted = Boolean.FALSE;

	@Column(nullable = false)
	private Integer status;

	@Column
	private Boolean requiresGrading = false;

	@OneToMany(mappedBy = "tests", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Questions> questions;

	@OneToMany(mappedBy = "tests", cascade = CascadeType.ALL)
	private Set<StudentTestResult> testCodeStudentTestResults;

	@OneToMany(mappedBy = "tests", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<TestClass> testClasses;

}
