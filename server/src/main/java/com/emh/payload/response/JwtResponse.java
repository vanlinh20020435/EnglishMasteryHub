package com.emh.payload.response;

import com.emh.controller.PropertyController;

import java.io.IOException;

public class JwtResponse
{
	private AccessToken accessToken;

	public JwtResponse(String accessToken) throws IOException
	{
		this.accessToken = new AccessToken(accessToken);
	}

	public AccessToken getAccessToken()
	{
		return accessToken;
	}

	public void setAccessToken(AccessToken accessToken)
	{
		this.accessToken = accessToken;
	}

	private static class AccessToken
	{
		public String token;
		public Long expires;

		public AccessToken(String token) throws IOException
		{
			this.token = token;
			expires = System.currentTimeMillis() + PropertyController.JWT_EXPIRATION_MS;
		}
	}
}
