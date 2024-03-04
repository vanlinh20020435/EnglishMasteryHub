package com.emh.payload.response;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuestFileResponse
{
	private Integer id;

	@Size(max = 255)
	private String type;

	@Size(max = 255)
	private String url;

	@Size(max = 255)
	private String name;
}
