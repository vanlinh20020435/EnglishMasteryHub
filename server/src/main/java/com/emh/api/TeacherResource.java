package com.emh.api;

import com.emh.payload.request.TeacherRequest;
import com.emh.payload.response.AdminResponse;
import com.emh.payload.response.ClassesResponse;
import com.emh.payload.response.TeacherResponse;
import com.emh.service.TeacherService;
import com.emh.util.ReferencedException;
import com.emh.util.ReferencedWarning;
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
@RequestMapping(value = "/api/teachers", produces = MediaType.APPLICATION_JSON_VALUE)
public class TeacherResource
{

	private final TeacherService teacherService;

	public TeacherResource(final TeacherService teacherService)
	{
		this.teacherService = teacherService;
	}

	@GetMapping
	@Secured("ROLE_ADMIN")
	public ResponseEntity<List<TeacherResponse>> getAllTeachers()
	{
		return ResponseEntity.ok(teacherService.findAll());
	}

	@GetMapping("/{teacherId}")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
	public ResponseEntity<TeacherResponse> getTeacher(
			@PathVariable(name = "teacherId") final Integer teacherId)
	{
		return ResponseEntity.ok(teacherService.get(teacherId));
	}

	@PostMapping
	@Secured("ROLE_ADMIN")
	@ApiResponse(responseCode = "201")
	public ResponseEntity<Integer> createTeacher(@RequestBody @Valid final TeacherRequest teacherDTO)
	{
		final Integer createdTeacherId = teacherService.create(teacherDTO);
		return new ResponseEntity<>(createdTeacherId, HttpStatus.CREATED);
	}

	@PutMapping("/{teacherId}")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
	public ResponseEntity<Integer> updateTeacher(
			@PathVariable(name = "teacherId") final Integer teacherId,
			@RequestBody @Valid final TeacherRequest teacherDTO)
	{
		teacherService.update(teacherId, teacherDTO);
		return ResponseEntity.ok(teacherId);
	}

	@DeleteMapping("/{teacherId}")
	@Secured("ROLE_ADMIN")
	@ApiResponse(responseCode = "204")
	public ResponseEntity<Void> deleteTeacher(
			@PathVariable(name = "teacherId") final Integer teacherId)
	{
		final ReferencedWarning referencedWarning = teacherService.getReferencedWarning(teacherId);
		if (referencedWarning != null)
		{
			throw new ReferencedException(referencedWarning);
		}
		teacherService.delete(teacherId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{teacherId}/classes/get-all")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
	public ResponseEntity<List<ClassesResponse>> getAllClasss(@PathVariable(name = "teacherId") final Integer teacherId)
	{
		return ResponseEntity.ok(teacherService.getAllClasss(teacherId));
	}

	@GetMapping("/search")
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<List<TeacherResponse>> searchTeacher(@RequestParam(required = false) String username,
														   @RequestParam(required = false) String email,
														   @RequestParam(required = false) String name) throws Exception
	{
		return ResponseEntity.ok(teacherService.searchTeacher(username, email, name));
	}
}
