package com.emh.payload.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ClassesResponse
{

	private Integer classId;

	@NotNull
	@Size(max = 50)
	private String className;

	private String avatar;

	private String description;

	@NotNull
	private TeacherResponse teacher;

}
