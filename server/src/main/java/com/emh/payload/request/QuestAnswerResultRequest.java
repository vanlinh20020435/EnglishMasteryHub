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

	private Boolean rightAnswer;

	private Double score;

	@NotNull
	private Double defaultScore;

	private Integer questionId;
}
