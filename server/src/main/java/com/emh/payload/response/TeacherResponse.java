package com.emh.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class TeacherResponse
{
	private Integer teacherId;

	@NotNull
	@Size(max = 16)
	private String username;

	@Size(max = 50)
	private String email;

	@NotNull
	@Size(max = 50)
	private String name;

	private Integer gender;

	@Size(max = 255)
	private String avatar;

	private Integer status;

	@JsonSerialize(as = Date.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date birthday;
}
