package com.emh.payload.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class QuestionsRequest
{
	@NotNull
	private String content;

	private String description;

	private String title;

	@NotNull
	@Size(max = 50)
	private String type;

	@NotNull
	@Size(max = 50)
	private String skill;

	private Integer time;

	@Valid
	private List<QuestionsRequest> subQuestions;

	@Valid
	private List<QuestAnswerRequest> answers;

	@Valid
	private List<QuestOptionRequest> options;

	@Valid
	private List<QuestFileRequest> files;
}
