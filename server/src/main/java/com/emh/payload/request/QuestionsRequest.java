package com.emh.payload.request;

import com.emh.payload.response.QuestOptionResponse;
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

	private String explanation;

	@NotNull
	@Size(max = 50)
	private String type;

	@NotNull
	@Size(max = 50)
	private String skill;

	private Integer time;

	private List<QuestionsRequest> subQuestions;

	private List<QuestAnswerRequest> answers;

	private List<QuestOptionRequest> options;

	private List<QuestFileRequest> files;
}
