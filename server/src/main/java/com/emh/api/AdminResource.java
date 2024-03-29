package com.emh.api;

import com.emh.payload.request.AdminRequest;
import com.emh.payload.response.AdminResponse;
import com.emh.service.AdminService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@Secured("ROLE_ADMIN")
@RequestMapping(value = "/api/admins", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminResource
{

	private final AdminService adminService;

	public AdminResource(final AdminService adminService)
	{
		this.adminService = adminService;
	}

	@GetMapping
	public ResponseEntity<List<AdminResponse>> getAllAdmins()
	{
		return ResponseEntity.ok(adminService.findAll());
	}

	@GetMapping("/{adminId}")
	public ResponseEntity<AdminResponse> getAdmin(
			@PathVariable(name = "adminId") final Integer adminId)
	{
		return ResponseEntity.ok(adminService.get(adminId));
	}

	@PostMapping
	@ApiResponse(responseCode = "201")
	public ResponseEntity<Integer> createAdmin(@RequestBody @Valid final AdminRequest adminRequest)
	{
		final Integer createdAdminId = adminService.create(adminRequest);
		return new ResponseEntity<>(createdAdminId, HttpStatus.CREATED);
	}

	@PutMapping("/{adminId}")
	public ResponseEntity<Integer> updateAdmin(
			@PathVariable(name = "adminId") final Integer adminId,
			@RequestBody @Valid final AdminRequest adminRequest)
	{
		adminService.update(adminId, adminRequest);
		return ResponseEntity.ok(adminId);
	}

	@DeleteMapping("/{adminId}")
	@ApiResponse(responseCode = "204")
	public ResponseEntity<Void> deleteAdmin(@PathVariable(name = "adminId") final Integer adminId)
	{
		adminService.delete(adminId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/search")
	public ResponseEntity<List<AdminResponse>> searchAdmin(@RequestParam(required = false) String username,
														   @RequestParam(required = false) String email,
														   @RequestParam(required = false) String name) throws Exception
	{
		return ResponseEntity.ok(adminService.searchAdmin(username, email, name));
	}

	@PutMapping("/{adminId}/update-status")
	@ApiResponse(responseCode = "201")
	public ResponseEntity<Integer> updateStatus(@PathVariable(name = "adminId") final Integer adminId,
												@RequestParam Integer status)
	{
		adminService.updateStatus(adminId, status);
		return new ResponseEntity<>(adminId, HttpStatus.CREATED);
	}

	@PutMapping("/{adminId}/update-password")
	@ApiResponse(responseCode = "201")
	public ResponseEntity<Integer> updatePassword(@PathVariable(name = "adminId") final Integer adminId,
												  @RequestParam String password)
	{
		adminService.updatePassword(adminId, password);
		return new ResponseEntity<>(adminId, HttpStatus.CREATED);
	}
}
