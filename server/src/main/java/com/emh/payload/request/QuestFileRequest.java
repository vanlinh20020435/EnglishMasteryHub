package com.emh.payload.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuestFileRequest
{
	@Size(max = 255)
	private String type;

	@Size(max = 255)
	private String url;

	@Size(max = 255)
	private String name;
}
