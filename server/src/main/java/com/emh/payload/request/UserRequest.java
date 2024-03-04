package com.emh.payload.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class UserRequest
{
	@NotNull
	@Size(max = 16)
	private String username;

	@Size(max = 50)
	private String email;

	@Size
	private String password;

	@NotNull
	@Size(max = 50)
	private String name;

	private Integer gender;

	@Size(max = 255)
	private String avatar;

	private LocalDate birthday;
}