package com.emh.api;

import com.emh.entity.RefreshToken;
import com.emh.entity.User;
import com.emh.payload.request.AuthRequest;
import com.emh.payload.request.TokenRefreshRequest;
import com.emh.payload.response.JwtResponse;
import com.emh.payload.response.MessageResponse;
import com.emh.service.RefreshTokenService;
import com.emh.service.UserDetailsImpl;
import com.emh.util.JwtUtils;
import com.emh.util.TokenRefreshException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
public class Auth
{
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	RefreshTokenService refreshTokenService;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<JwtResponse> authenticateUser(@RequestBody AuthRequest request) throws IOException
	{
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		String jwt = jwtUtils.generateJwtToken(userDetails);
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
		return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken()));
	}

	@PostMapping("/signout")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"})
	public ResponseEntity<MessageResponse> logoutUser()
	{
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(new MessageResponse("Log out successful!"));
	}

	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request)
	{
		String requestRefreshToken = request.getRefreshToken();

		return refreshTokenService.findByToken(requestRefreshToken)
				.map(refreshTokenService::verifyExpiration)
				.map(refreshTokenService::renewToken)
				.map(refreshToken -> {
					User user = refreshToken.getUser();
					String token = jwtUtils.generateTokenFromUsername(user.getUsername());
					return ResponseEntity.ok(new JwtResponse(token, refreshToken.getToken()));
				})
				.orElseThrow(() -> new TokenRefreshException("Refresh token is not in database!"));
	}
}
