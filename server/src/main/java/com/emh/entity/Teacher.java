package com.emh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Teacher
{

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer teacherId;

	@Column(nullable = false, length = 16)
	private String username;

	@Column(length = 50)
	private String email;

	@Column(nullable = false, length = 32)
	private String password;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false)
	private OffsetDateTime lastLogin;

	@Column(nullable = false)
	private Integer genderId;

	@Column
	private String avatar;

	@Column(nullable = false)
	private LocalDate birthday;

	@OneToMany(mappedBy = "teacher")
	private Set<Classs> teacherClasses;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "permission_id")
	private Permission permission;

	@OneToMany(mappedBy = "teacher")
	private Set<TeacherNotification> teacherTeacherNotifications;

}
