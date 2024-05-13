package com.emh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Entity
@Getter
@Setter
public class Questions extends BaseEntity
{

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer questionId;

	@Column(nullable = false, columnDefinition = "longtext")
	private String content;

	@Column(nullable = false)
	private Integer status = 1;

	@Column(nullable = false, length = 50)
	private String type;

	@Column(nullable = false, length = 50)
	private String skill;

	@Column
	private Integer time;

	@Column(columnDefinition = "longtext")
	private String description;

	@Column(columnDefinition = "longtext")
	private String title;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_id", nullable = false)
	private Tests tests;

	@OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<QuestAnswer> questionQuestAnswers;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_question_id")
	private Questions parentQuestion;

	@OneToMany(mappedBy = "parentQuestion", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Questions> questions;

	@OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<QuestOption> questOptions;

	@OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<QuestFile> questFiles;

}
