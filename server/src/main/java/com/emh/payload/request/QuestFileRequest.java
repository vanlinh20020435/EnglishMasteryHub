package com.emh.payload.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuestFileRequest
{
	@NotNull
	@Size(max = 255)
	private String type;

	@NotNull
	@Size(max = 255)
	private String url;

	@NotNull
	@Size(max = 255)
	private String name;
}
