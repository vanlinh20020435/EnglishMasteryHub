package com.emh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;


@Entity
@Getter
@Setter
public class Student extends BaseEntity
{

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer studentId;

	@Column(nullable = false, length = 16, unique = true)
	private String username;

	@Column(length = 50)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false)
	private Integer gender;

	@Column(nullable = false)
	private Integer status;

	@Column
	private String avatar;

	@Column
	@Temporal(TemporalType.DATE)
	private Date birthday;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "class_id", nullable = false)
	private Classes classs;

	@OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE)
	private Set<StudentTestResult> StudentTestResults;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

}
