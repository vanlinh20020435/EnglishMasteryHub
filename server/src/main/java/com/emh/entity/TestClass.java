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
	TestClassKey id = new TestClassKey();

	@MapsId("classId")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "class_id", nullable = false)
	private Classes classs;

	@MapsId("testId")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "test_id", nullable = false)
	private Tests tests;
}

@Getter
@Setter
@Embeddable
class TestClassKey implements Serializable
{
	Integer classId;
	Integer testId;
}
