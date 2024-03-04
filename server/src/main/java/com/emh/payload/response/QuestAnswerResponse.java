package com.emh.payload.response;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuestAnswerResponse
{
	@NotNull
	private String answer;

	@NotNull
	private String explanation;
}
