package com.emh.payload.response;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuestOptionResponse
{
	@NotNull
	private String option;
}
