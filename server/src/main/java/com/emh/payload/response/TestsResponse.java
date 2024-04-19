package com.emh.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
public class TestsResponse
{

	private Integer testId;

	@NotNull
	@Size(max = 255)
	private String testName;

	@Size(max = 32)
	private String password;

	private Integer totalQuestions;

	private Integer time;

	@NotNull
	private String description;

	@NotNull
	private Integer status;

	private Boolean requiresGrading = false;

	@JsonSerialize(as = Date.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonProperty("created")
	private Date createdDate;

	private String creator;

	private Boolean havePermission;

	@NotNull
	private List<QuestionsResponse> questions = new ArrayList<>();
}
