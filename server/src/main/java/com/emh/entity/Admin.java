package com.emh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Entity
@Table(name = "\"Admin\"")
@Getter
@Setter
public class Admin extends BaseEntity
{

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer adminId;

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
	private Date birthday;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

}
