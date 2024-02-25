package com.emh.api;

import com.emh.payload.request.TeacherRequest;
import com.emh.payload.response.TeacherResponse;
import com.emh.service.TeacherService;
import com.emh.util.ReferencedException;
import com.emh.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
	public ResponseEntity<List<TeacherResponse>> getAllTeachers()
	{
		return ResponseEntity.ok(teacherService.findAll());
	}

	@GetMapping("/{teacherId}")
	public ResponseEntity<TeacherResponse> getTeacher(
			@PathVariable(name = "teacherId") final Integer teacherId)
	{
		return ResponseEntity.ok(teacherService.get(teacherId));
	}

	@PostMapping
	@ApiResponse(responseCode = "201")
	public ResponseEntity<Integer> createTeacher(@RequestBody @Valid final TeacherRequest teacherDTO)
	{
		final Integer createdTeacherId = teacherService.create(teacherDTO);
		return new ResponseEntity<>(createdTeacherId, HttpStatus.CREATED);
	}

	@PutMapping("/{teacherId}")
	public ResponseEntity<Integer> updateTeacher(
			@PathVariable(name = "teacherId") final Integer teacherId,
			@RequestBody @Valid final TeacherRequest teacherDTO)
	{
		teacherService.update(teacherId, teacherDTO);
		return ResponseEntity.ok(teacherId);
	}

	@DeleteMapping("/{teacherId}")
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

}
