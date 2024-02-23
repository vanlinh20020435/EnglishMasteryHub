package com.emh.api;

import com.emh.payload.request.AuthRequest;
import com.emh.payload.response.JwtResponse;
import com.emh.payload.response.MessageResponse;
import com.emh.service.UserDetailsImpl;
import com.emh.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class Auth
{
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest request) throws IOException
	{
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		String jwt = jwtUtils.generateJwtToken(userDetails);
		return ResponseEntity.ok(new JwtResponse(jwt));
	}

	@PostMapping("/signout")
	public ResponseEntity<?> logoutUser()
	{
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(new MessageResponse("Log out successful!"));
	}
}
