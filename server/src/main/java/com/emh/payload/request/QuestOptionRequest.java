package com.emh.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuestOptionRequest
{
	@NotNull
	private String option;
}
