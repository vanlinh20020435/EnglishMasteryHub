package com.emh.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenRefreshException extends RuntimeException
{

	public TokenRefreshException()
	{
		super();
	}

	public TokenRefreshException(final String message)
	{
		super(message);
	}

}