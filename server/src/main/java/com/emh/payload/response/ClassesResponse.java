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
public class ClassesResponse
{

	private Integer classId;

	@NotNull
	@Size(max = 50)
	private String className;

	private String avatar;

	private String description;

	@JsonSerialize(as = Date.class)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	private Date startDate;

	@JsonSerialize(as = Date.class)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	private Date endDate;

	@NotNull
	private TeacherResponse teacher;

}
