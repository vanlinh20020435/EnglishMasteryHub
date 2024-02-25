package com.emh.payload.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class UserResponse
{
	private Integer userId;

	@NotNull
	@Size(max = 16)
	private String username;

	@Size(max = 50)
	private String email;

	private String password;

	private String role;

	@NotNull
	@Size(max = 50)
	private String name;

	private Integer gender;

	private Integer status;

	@Size(max = 255)
	private String avatar;

	private LocalDate birthday;
}
