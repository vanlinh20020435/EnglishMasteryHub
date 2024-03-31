package com.emh.service;

import com.emh.controller.PropertyController;
import com.emh.entity.RefreshToken;
import com.emh.entity.User;
import com.emh.repos.RefreshTokenRepository;
import com.emh.repos.UserRepository;
import com.emh.util.NotFoundException;
import com.emh.util.TokenRefreshException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService
{
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private UserRepository userRepository;

	public Optional<RefreshToken> findByToken(String token)
	{
		return refreshTokenRepository.findByToken(token);
	}

	public RefreshToken createRefreshToken(Integer userId)
	{
		RefreshToken refreshToken = new RefreshToken();
		User user = userRepository.findById(userId)
				.orElseThrow(NotFoundException::new);
		boolean tokenExists = user.getRefreshToken() != null;
		if (tokenExists)
			refreshToken.setId(user.getRefreshToken().getId());
		refreshToken.setExpiryDate(Instant.now().plusMillis(PropertyController.JWT_REFRESH_EXPIRATION_MS));
		refreshToken.setToken(generateToken());
		refreshToken = refreshTokenRepository.save(refreshToken);
		if (!tokenExists)
		{
			user.setRefreshToken(refreshToken);
			userRepository.save(user);
		}
		return refreshToken;
	}

	public RefreshToken verifyExpiration(RefreshToken token)
	{
		if (token.getExpiryDate().compareTo(Instant.now()) < 0)
		{
			refreshTokenRepository.delete(token);
			throw new TokenRefreshException("Refresh token was expired. Please make a new signin request");
		}
		return token;
	}

	public RefreshToken renewToken(RefreshToken token)
	{
		token.setToken(generateToken());
		token = refreshTokenRepository.save(token);
		return token;
	}

	@Transactional
	public int deleteByUserId(Integer userId)
	{
		return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
	}

	public String generateToken()
	{
		String token = System.currentTimeMillis() + UUID.randomUUID().toString();
		token = Base64.getEncoder().encodeToString(token.getBytes());
		return token;
	}
}