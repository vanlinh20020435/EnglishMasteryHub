package com.emh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "\"Admin\"")
@Getter
@Setter
public class Admin
{

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer adminId;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "permission_id")
	private Permission permission;

}
