package com.emh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"question_id", "test_result_id"})})
public class QuestAnswerResult extends BaseEntity
{

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ElementCollection(fetch = FetchType.EAGER)
	@Column(nullable = false, length = 8192)
	private List<String> answers;

	@Column
	private Boolean rightAnswer;

	@Column
	private Double score;

	@Column
	private Double defaultScore;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "question_id", nullable = false)
	private Questions question;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "test_result_id", nullable = false)
	private StudentTestResult studentTestResult;
}
