package com.emh.api;

import com.emh.payload.request.StudentRequest;
import com.emh.payload.response.StudentResponse;
import com.emh.service.StudentService;
import com.emh.util.ReferencedException;
import com.emh.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
	public ResponseEntity<List<StudentResponse>> getAllStudents()
	{
		return ResponseEntity.ok(studentService.findAll());
	}

	@GetMapping("/{studentId}")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"})
	public ResponseEntity<StudentResponse> getStudent(
			@PathVariable(name = "studentId") final Integer studentId)
	{
		return ResponseEntity.ok(studentService.get(studentId));
	}

	@GetMapping("/find-by-user/{userId}")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"})
	public ResponseEntity<StudentResponse> getStudentByUser(@PathVariable(name = "userId") final Integer userId)
	{
		return ResponseEntity.ok(studentService.getByUserId(userId));
	}

	@PostMapping
	@ApiResponse(responseCode = "201")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<Integer> createStudent(@RequestBody @Valid final StudentRequest studentRequest)
	{
		final Integer createdStudentId = studentService.create(studentRequest);
		return new ResponseEntity<>(createdStudentId, HttpStatus.CREATED);
	}

	@PutMapping("/{studentId}")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"})
	public ResponseEntity<Integer> updateStudent(
			@PathVariable(name = "studentId") final Integer studentId,
			@RequestBody @Valid final StudentRequest studentRequest)
	{
		studentService.update(studentId, studentRequest);
		return ResponseEntity.ok(studentId);
	}

	@DeleteMapping("/{studentId}")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
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

	@GetMapping("/search")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
	public ResponseEntity<List<StudentResponse>> searchStudent(@RequestParam(required = false) String username,
															   @RequestParam(required = false) String email,
															   @RequestParam(required = false) String name,
															   @RequestParam(required = false) Integer classId) throws Exception
	{
		return ResponseEntity.ok(studentService.searchStudent(username, email, name, classId));
	}

	@PutMapping("/{studentId}/update-status")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
	@ApiResponse(responseCode = "201")
	public ResponseEntity<Integer> updateStatus(@PathVariable(name = "studentId") final Integer studentId,
												@RequestParam Integer status)
	{
		studentService.updateStatus(studentId, status);
		return new ResponseEntity<>(studentId, HttpStatus.CREATED);
	}

	@PutMapping("/{studentId}/update-password")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
	@ApiResponse(responseCode = "201")
	public ResponseEntity<Integer> updatePassword(@PathVariable(name = "studentId") final Integer studentId,
												  @RequestParam String password)
	{
		studentService.updatePassword(studentId, password);
		return new ResponseEntity<>(studentId, HttpStatus.CREATED);
	}

	@Operation(summary = "Import file to create student", description = "Support xls/xlsx and csv/txt files (columns separated by ',') files include 2 columns fullname and gender. The account's default password is '123456'")
	@PostMapping("/import/{classId}")
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<List<Integer>> uploadFile(@RequestParam("file") MultipartFile file,
													@PathVariable(name = "classId") final Integer classId) throws Exception
	{
		return ResponseEntity.ok(studentService.importFile(file, classId));
	}
}
