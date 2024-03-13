package com.emh.payload.response;

import com.emh.controller.PropertyController;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse
{
	private Token accessToken;
	private Token refreshToken;

	public JwtResponse(String accessToken, String refreshToken)
	{
		Long now = System.currentTimeMillis();
		this.accessToken = new Token(accessToken, now + PropertyController.JWT_EXPIRATION_MS);
		this.refreshToken = new Token(refreshToken, now + PropertyController.JWT_REFRESH_EXPIRATION_MS);
	}

	private static class Token
	{
		public String token;
		public Long expires;

		public Token(String token, Long expires)
		{
			this.token = token;
			this.expires = expires;
		}
	}
}
