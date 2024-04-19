package com.emh.payload.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class StudentTestResultRequest
{
	private Double score;

	@NotNull
	private Double testDefaultScore;

	@NotNull
	private Integer time;

	@NotNull
	private Integer classId;

	@NotNull
	private Integer testId;

	@NotNull
	private Integer studentId;

	@Valid
	@NotNull
	private List<QuestAnswerResultRequest> questionResults;
}
