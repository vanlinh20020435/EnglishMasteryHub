package com.emh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class QuestFile extends BaseEntity
{

	@Id
	@Column(nullable = false, updatable = false)
	private String type;

	@Column(nullable = false)
	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id", nullable = false)
	private Questions question;

}
