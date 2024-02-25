package com.emh.api;

import com.emh.service.UserDetailsImpl;
import com.emh.service.UserService;
import com.emh.util.SecurityUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserResource
{

	private final UserService userService;

	public UserResource(final UserService userService)
	{
		this.userService = userService;
	}

	@GetMapping("/userinfo")
	public ResponseEntity<?> getUserInfo()
	{
		UserDetailsImpl userDetails = SecurityUtils.getPrincipal();
		return ResponseEntity.ok(userService.get(userDetails.getId()));
	}
}
