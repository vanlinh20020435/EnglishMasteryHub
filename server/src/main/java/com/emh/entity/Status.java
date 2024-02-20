package com.emh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Status
{

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer statusId;

	@Column(nullable = false, length = 50)
	private String detail;

	@OneToMany(mappedBy = "status")
	private Set<Question> statusQuestions;

	@OneToMany(mappedBy = "status")
	private Set<Test> statusTests;

}
