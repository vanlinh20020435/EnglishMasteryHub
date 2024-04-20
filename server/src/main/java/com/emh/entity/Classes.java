package com.emh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
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

	@Column
	private String avatar;

	@Column
	private String description;

	@Column(name = "start_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Column(name = "end_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "teacher_id", nullable = false)
	private Teacher teacher;

	@OneToMany(mappedBy = "classs", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Student> classStudents;

	@OneToMany(mappedBy = "classs", cascade = CascadeType.ALL)
	private Set<StudentNotifications> classStudentNotificationses;

	@OneToMany(mappedBy = "classs", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<TestClass> testClasses;

	@OneToMany(mappedBy = "classs", cascade = CascadeType.ALL)
	private Set<ClassFile> classFiles;

	@OneToMany(mappedBy = "classs", cascade = CascadeType.ALL)
	private Set<StudentTestResult> classStudentTestResults;
}
