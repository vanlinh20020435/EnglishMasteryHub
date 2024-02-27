package com.emh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;


@Entity
@Getter
@Setter
public class Teacher extends BaseEntity
{

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer teacherId;

	@Column(nullable = false, length = 16)
	private String username;

	@Column(length = 50)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, length = 50)
	private String name;

	@Column
	private OffsetDateTime lastLogin;

	@Column(nullable = false)
	private Integer gender;

	@Column(nullable = false)
	private Integer status;

	@Column
	private String avatar;

	@Column
	private LocalDate birthday;

	@OneToMany(mappedBy = "teacher")
	private Set<Classes> classes;

	@OneToMany(mappedBy = "teacher", cascade = CascadeType.REMOVE)
	private Set<TeacherNotifications> teacherNotificationses;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

}
