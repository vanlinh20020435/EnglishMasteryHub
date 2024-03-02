package com.emh.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
public class QuestAnswerResult extends BaseEntity
{

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private List<String> answers;

	@Column
	private Boolean rightAnswer;

	@Column
	private Integer score;

	@Column
	private Integer defaultScore;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id", nullable = false)
	private Questions question;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_result_id", nullable = false)
	private StudentTestResult studentTestResult;
}
