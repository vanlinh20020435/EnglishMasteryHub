package com.emh.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class QuestAnswerResultRequest
{
	@NotNull
	private List<String> answers;

	@NotNull
	private Boolean rightAnswer;

	private Integer score;

	@NotNull
	private Integer defaultScore;

	private Integer questionId;
}
