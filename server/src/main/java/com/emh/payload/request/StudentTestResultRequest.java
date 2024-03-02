package com.emh.payload.request;

import com.emh.entity.QuestAnswerResult;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class StudentTestResultRequest
{
	private Integer score;

	@NotNull
	private Integer testDefaultScore;

	@NotNull
	private Integer time;

	@NotNull
	private Integer testId;

	@NotNull
	private Integer studentId;

	@NotNull
	private List<QuestAnswerResult> questionResults;
}
