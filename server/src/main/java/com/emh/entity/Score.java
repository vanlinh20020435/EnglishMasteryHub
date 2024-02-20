package com.emh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Score
{

	@Id
	@Column(nullable = false, updatable = false, length = 10)
	private String scoreNumber;

	@Column(nullable = false, length = 50)
	private String scoreDetail;

	@Column
	private OffsetDateTime completionTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_code_id", nullable = false)
	private Test testCode;

}
