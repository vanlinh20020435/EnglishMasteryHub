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
public class Permission
{

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer permission;

	@Column(nullable = false, length = 20)
	private String permissionDetail;

	@OneToMany(mappedBy = "permission")
	private Set<Admin> permissionAdmins;

	@OneToMany(mappedBy = "permission")
	private Set<Student> permissionStudents;

	@OneToMany(mappedBy = "permission")
	private Set<Teacher> permissionTeachers;

}
