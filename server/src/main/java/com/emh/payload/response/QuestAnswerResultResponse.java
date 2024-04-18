package com.emh.payload.response;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class QuestAnswerResultResponse
{
	private Long id;

	@NotNull
	private Boolean rightAnswer;

	private Double score;

	@NotNull
	private Double defaultScore;

	private QuestionsResponse question;

	@NotNull
	private List<String> answers;
}
