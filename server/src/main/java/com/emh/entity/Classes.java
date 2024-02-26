package com.emh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Entity
@Getter
@Setter
public class Classes extends BaseEntity
{

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer classId;

	@Column(nullable = false, length = 50)
	private String className;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "teacher_id", nullable = false)
	private Teacher teacher;

	@OneToMany(mappedBy = "classs")
	private Set<Student> classStudents;

	@OneToMany(mappedBy = "classs", cascade = CascadeType.REMOVE)
	private Set<StudentNotifications> classStudentNotificationses;

	@OneToMany(mappedBy = "classs", cascade = CascadeType.REMOVE)
	private Set<TestClass> testClasses;
}
