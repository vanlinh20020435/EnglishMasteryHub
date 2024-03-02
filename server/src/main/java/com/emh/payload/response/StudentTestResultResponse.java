package com.emh.payload.response;

import com.emh.entity.QuestAnswerResult;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class StudentTestResultResponse
{
	private Integer id;

	private Integer score;

	@NotNull
	private Integer testDefaultScore;

	@NotNull
	private Integer time;

	@NotNull
	private TestsResponse test;

	@NotNull
	private List<QuestAnswerResultResponse> questionResults;
}
