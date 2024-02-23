package com.emh.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest
{
	private long id;
	private String username;
	private String password;
	private String role;
}