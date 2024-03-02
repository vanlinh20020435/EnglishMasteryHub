package com.emh.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class AdminRequest
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

	@JsonSerialize(as = Date.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date birthday;
}
