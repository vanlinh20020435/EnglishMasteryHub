package com.emh.payload.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ClassesRequest
{
	@NotNull
	@Size(max = 50)
	private String className;

	@NotNull
	private Integer teacherId;
}
