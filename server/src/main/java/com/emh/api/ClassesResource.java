package com.emh.api;

import com.emh.payload.request.ClassFileRequest;
import com.emh.payload.request.ClassesRequest;
import com.emh.payload.request.TestClassRequest;
import com.emh.payload.response.*;
import com.emh.service.ClassFileService;
import com.emh.service.ClassesService;
import com.emh.service.TestClassService;
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
@RequestMapping(value = "/api/class", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClassesResource
{

	private final ClassesService classesService;
	private final TestClassService testClassService;
	private final ClassFileService classFileService;

	public ClassesResource(final ClassesService classesService,
						   final TestClassService testClassService,
						   final ClassFileService classFileService)
	{
		this.classesService = classesService;
		this.testClassService = testClassService;
		this.classFileService = classFileService;
	}

	@GetMapping
	@Secured("ROLE_ADMIN")
	public ResponseEntity<List<ClassesResponse>> getAllClassess()
	{
		return ResponseEntity.ok(classesService.findAll());
	}

	@GetMapping("/{classId}")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
	public ResponseEntity<ClassesResponse> getClasses(
			@PathVariable(name = "classId") final Integer classId)
	{
		return ResponseEntity.ok(classesService.get(classId));
	}

	@PostMapping
	@Secured("ROLE_ADMIN")
	@ApiResponse(responseCode = "201")
	public ResponseEntity<Integer> createClasses(@RequestBody @Valid final ClassesRequest classesRequest)
	{
		final Integer createdClassId = classesService.create(classesRequest);
		return new ResponseEntity<>(createdClassId, HttpStatus.CREATED);
	}

	@PutMapping("/{classId}")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<Integer> updateClasses(
			@PathVariable(name = "classId") final Integer classId,
			@RequestBody @Valid final ClassesRequest classesRequest)
	{
		classesService.update(classId, classesRequest);
		return ResponseEntity.ok(classId);
	}

	@DeleteMapping("/{classId}")
	@Secured("ROLE_ADMIN")
	@ApiResponse(responseCode = "204")
	public ResponseEntity<Void> deleteClasses(
			@PathVariable(name = "classId") final Integer classId)
	{
		final ReferencedWarning referencedWarning = classesService.getReferencedWarning(classId);
		if (referencedWarning != null)
		{
			throw new ReferencedException(referencedWarning);
		}
		classesService.delete(classId);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{classId}/tests/{testId}")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
	@ApiResponse(responseCode = "201")
	public ResponseEntity<Void> addTestToClass(@PathVariable(name = "classId") final Integer classId,
											   @PathVariable(name = "testId") final Integer testId,
											   @RequestBody final TestClassRequest testClassRequest)
	{
		testClassService.create(classId, testId, testClassRequest);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{classId}/tests/get-all")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
	@ApiResponse(responseCode = "201")
	public ResponseEntity<List<TestClassResponse>> getAllTest(@PathVariable(name = "classId") final Integer classId)
	{
		return ResponseEntity.ok(testClassService.findAllByClass(classId));
	}

	@GetMapping("/{classId}/tests-info/get-all")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"})
	@ApiResponse(responseCode = "201")
	public ResponseEntity<List<TestClassInfoResponse>> getAllTestInfo(@PathVariable(name = "classId") final Integer classId)
	{
		return ResponseEntity.ok(testClassService.findAllInfoByClass(classId));
	}

	@GetMapping("/{classId}/tests-info/{testId}")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"})
	public ResponseEntity<TestClassInfoResponse> getTestsInfo(
			@PathVariable(name = "classId") final Integer classId,
			@PathVariable(name = "testId") final Integer testId)
	{
		return ResponseEntity.ok(testClassService.getTestInfo(classId, testId));
	}

	@DeleteMapping("/{classId}/tests/{testId}")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
	@ApiResponse(responseCode = "204")
	public ResponseEntity<Void> deleteTests(@PathVariable(name = "classId") final Integer classId,
											@PathVariable(name = "testId") final Integer testId)
	{
		testClassService.delete(classId, testId);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{classId}/files")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
	@ApiResponse(responseCode = "201")
	public ResponseEntity<Integer> addFileToClass(@PathVariable(name = "classId") final Integer classId,
												  @RequestBody @Valid final ClassFileRequest classFileRequest)
	{
		final Integer fileId = classFileService.create(classId, classFileRequest);
		return new ResponseEntity<>(fileId, HttpStatus.CREATED);
	}

	@GetMapping("/{classId}/files/get-all")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"})
	@ApiResponse(responseCode = "201")
	public ResponseEntity<List<ClassFileResponse>> getAllFiles(@PathVariable(name = "classId") final Integer classId)
	{
		return ResponseEntity.ok(classFileService.findAllByClass(classId));
	}


	@DeleteMapping("/{classId}/files/{fileId}")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
	@ApiResponse(responseCode = "204")
	public ResponseEntity<Void> deleteFile(@PathVariable(name = "classId") final Integer classId,
										   @PathVariable(name = "fileId") final Integer fileId)
	{
		classFileService.delete(classId, fileId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{classId}/students/get-all")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"})
	@ApiResponse(responseCode = "201")
	public ResponseEntity<List<StudentResponse>> getAllStudents(@PathVariable(name = "classId") final Integer classId)
	{
		return ResponseEntity.ok(classesService.getAllStudents(classId));
	}

	@GetMapping("/search")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
	public ResponseEntity<List<ClassesResponse>> searchClass(@RequestParam(required = false) String className,
															 @RequestParam(required = false) Integer teacherId) throws Exception
	{
		return ResponseEntity.ok(classesService.searchClass(className, teacherId));
	}
}
