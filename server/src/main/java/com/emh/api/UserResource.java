package com.emh.api;

import com.emh.payload.request.UserRequest;
import com.emh.service.UserDetailsImpl;
import com.emh.service.UserService;
import com.emh.util.SecurityUtils;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
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
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"})
	public ResponseEntity<?> getUserInfo()
	{
		UserDetailsImpl userDetails = SecurityUtils.getPrincipal();
		return ResponseEntity.ok(userService.get(userDetails.getId()));
	}

	@PutMapping("/update-password")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"})
	@ApiResponse(responseCode = "201")
	public ResponseEntity<Integer> updatePassword(@RequestParam String password)
	{
		UserDetailsImpl userDetails = SecurityUtils.getPrincipal();
		userService.updatePassword(userDetails.getId(), password);
		return new ResponseEntity<>(userDetails.getId(), HttpStatus.CREATED);
	}

	@PutMapping("/userinfo")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"})
	@ApiResponse(responseCode = "201")
	public ResponseEntity<Integer> updateUser(@RequestBody UserRequest userRequest)
	{
		UserDetailsImpl userDetails = SecurityUtils.getPrincipal();
		userService.update(userDetails.getId(), userRequest);
		return new ResponseEntity<>(userDetails.getId(), HttpStatus.CREATED);
	}
}
