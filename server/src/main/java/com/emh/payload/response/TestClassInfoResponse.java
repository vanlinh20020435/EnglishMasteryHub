package com.emh.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class TestClassInfoResponse
{

	private Integer testId;

	@NotNull
	@Size(max = 255)
	private String testName;

	private Integer totalQuestions;

	private Integer time;

	@NotNull
	private String description;

	@NotNull
	private Integer status;

	private boolean isPrivate;

	@JsonSerialize(as = Date.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonProperty("created")
	private Date createdDate;

	private String creator;

	private Boolean havePermission;

	@JsonSerialize(as = Date.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm")
	private Date startDate;

	@JsonSerialize(as = Date.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm")
	private Date endDate;
}
