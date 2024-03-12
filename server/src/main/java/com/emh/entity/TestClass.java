package com.emh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Entity
@Getter
@Setter
public class TestClass extends BaseEntity
{
	@EmbeddedId
	TestClassKey id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "class_id", nullable = false)
	private Classes classs;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "test_id", nullable = false)
	private Tests tests;

	@Embeddable
	class TestClassKey implements Serializable
	{

		@Column(name = "student_id")
		Long studentId;

		@Column(name = "course_id")
		Long courseId;
	}
}
