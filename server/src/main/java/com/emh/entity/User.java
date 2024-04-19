package com.emh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;


@Entity
@Getter
@Setter
public class User extends BaseEntity
{

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	@Column(nullable = false, length = 16, unique = true)
	private String username;

	@Column(length = 50)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false)
	private Integer status;

	@Column(nullable = false)
	private Integer gender;

	@Column
	private String avatar;

	@Column
	@Temporal(TemporalType.DATE)
	private Date birthday;

	@Column(nullable = false, name = "\"role\"", length = 50)
	private String role;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private Set<Admin> userAdmins;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private Set<Teacher> userTeachers;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private Set<Student> userStudents;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "refresh_token_id")
	private RefreshToken refreshToken;
}
