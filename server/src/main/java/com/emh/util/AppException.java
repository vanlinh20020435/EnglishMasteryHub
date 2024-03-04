package com.emh.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.CONFLICT)
public class AppException extends RuntimeException
{

	public AppException()
	{
		super();
	}

	public AppException(final String message)
	{
		super(message);
	}

}
