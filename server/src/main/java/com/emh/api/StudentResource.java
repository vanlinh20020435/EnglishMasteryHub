package com.emh.api;

import com.emh.payload.request.StudentRequest;
import com.emh.payload.request.TeacherRequest;
import com.emh.payload.response.StudentResponse;
import com.emh.service.StudentService;
import com.emh.util.ReferencedException;
import com.emh.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/students", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudentResource
{

	private final StudentService studentService;

	public StudentResource(final StudentService studentService)
	{
		this.studentService = studentService;
	}

	@GetMapping
	public ResponseEntity<List<StudentResponse>> getAllStudents()
	{
		return ResponseEntity.ok(studentService.findAll());
	}

	@GetMapping("/{studentId}")
	public ResponseEntity<StudentResponse> getStudent(
			@PathVariable(name = "studentId") final Integer studentId)
	{
		return ResponseEntity.ok(studentService.get(studentId));
	}

	@PostMapping
	@ApiResponse(responseCode = "201")
	public ResponseEntity<Integer> createStudent(@RequestBody @Valid final StudentRequest studentRequest)
	{
		final Integer createdStudentId = studentService.create(studentRequest);
		return new ResponseEntity<>(createdStudentId, HttpStatus.CREATED);
	}

	@PutMapping("/{studentId}")
	public ResponseEntity<Integer> updateStudent(
			@PathVariable(name = "studentId") final Integer studentId,
			@RequestBody @Valid final StudentRequest studentRequest)
	{
		studentService.update(studentId, studentRequest);
		return ResponseEntity.ok(studentId);
	}

	@DeleteMapping("/{studentId}")
	@ApiResponse(responseCode = "204")
	public ResponseEntity<Void> deleteStudent(
			@PathVariable(name = "studentId") final Integer studentId)
	{
		final ReferencedWarning referencedWarning = studentService.getReferencedWarning(studentId);
		if (referencedWarning != null)
		{
			throw new ReferencedException(referencedWarning);
		}
		studentService.delete(studentId);
		return ResponseEntity.noContent().build();
	}

}
