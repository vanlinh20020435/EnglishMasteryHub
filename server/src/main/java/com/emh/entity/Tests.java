package com.emh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
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

	@Column(length = 32)
	private String password;

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

	@Column(nullable = false)
	private OffsetDateTime timest;

	@OneToMany(mappedBy = "tests")
	private Set<Questions> questions;

	@OneToMany(mappedBy = "tests")
	private Set<Scores> scores;

	@OneToMany(mappedBy = "tests")
	private Set<StudentTestDetail> testCodeStudentTestDetails;

}
