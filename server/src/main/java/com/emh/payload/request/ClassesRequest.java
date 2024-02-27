package com.emh.payload.request;

import jakarta.persistence.Column;
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

	private String avatar;

	private String description;

	@NotNull
	private Integer teacherId;
}
