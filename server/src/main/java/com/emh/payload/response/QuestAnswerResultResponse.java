package com.emh.payload.response;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class QuestAnswerResultResponse
{
	private Integer id;

	@NotNull
	private Boolean rightAnswer;

	private Integer score;

	@NotNull
	private Integer defaultScore;

	private Integer questionId;

	@NotNull
	private List<String> answers;
}
