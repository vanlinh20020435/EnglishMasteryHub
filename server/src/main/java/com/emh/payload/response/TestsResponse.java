package com.emh.payload.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;


@Getter
@Setter
public class TestsResponse
{

	private Integer testId;

	@NotNull
	@Size(max = 255)
	private String testName;

	@Size(max = 32)
	private String password;

	private Integer totalQuestions;

	private Integer time;

	@NotNull
	private String description;

	@NotNull
	private Integer status;

	@NotNull
	private List<QuestionsResponse> questions;
}
