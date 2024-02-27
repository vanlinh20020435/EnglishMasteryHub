package com.emh.payload.response;

import com.emh.entity.QuestOption;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class QuestionsResponse
{

	private Integer questionId;

	@NotNull
	private String content;

	private String description;

	private String explanation;

	@NotNull
	@Size(max = 50)
	private String type;

	@NotNull
	@Size(max = 50)
	private String skill;

	private Integer time;

	private List<QuestionsResponse> subQuestions;

	private List<QuestAnswerResponse> answers;

	private List<QuestOptionResponse> options;
}
