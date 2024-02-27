package com.emh.api;

import com.emh.payload.request.ClassFileRequest;
import com.emh.payload.request.ClassesRequest;
import com.emh.payload.response.ClassFileResponse;
import com.emh.payload.response.ClassesResponse;
import com.emh.payload.response.TestsResponse;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/classess", produces = MediaType.APPLICATION_JSON_VALUE)
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
	public ResponseEntity<List<ClassesResponse>> getAllClassess()
	{
		return ResponseEntity.ok(classesService.findAll());
	}

	@GetMapping("/{classId}")
	public ResponseEntity<ClassesResponse> getClasses(
			@PathVariable(name = "classId") final Integer classId)
	{
		return ResponseEntity.ok(classesService.get(classId));
	}

	@PostMapping
	@ApiResponse(responseCode = "201")
	public ResponseEntity<Integer> createClasses(@RequestBody @Valid final ClassesRequest classesRequest)
	{
		final Integer createdClassId = classesService.create(classesRequest);
		return new ResponseEntity<>(createdClassId, HttpStatus.CREATED);
	}

	@PutMapping("/{classId}")
	public ResponseEntity<Integer> updateClasses(
			@PathVariable(name = "classId") final Integer classId,
			@RequestBody @Valid final ClassesRequest classesRequest)
	{
		classesService.update(classId, classesRequest);
		return ResponseEntity.ok(classId);
	}

	@DeleteMapping("/{classId}")
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
	@ApiResponse(responseCode = "201")
	public ResponseEntity<Void> addTestToClass(@PathVariable(name = "classId") final Integer classId,
											   @PathVariable(name = "testId") final Integer testId)
	{
		testClassService.create(classId, testId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{classId}/tests/get-all")
	@ApiResponse(responseCode = "201")
	public ResponseEntity<List<TestsResponse>> getAllTest(@PathVariable(name = "classId") final Integer classId)
	{
		return ResponseEntity.ok(testClassService.findAllByClass(classId));
	}


	@DeleteMapping("/{classId}/tests/{testId}")
	@ApiResponse(responseCode = "204")
	public ResponseEntity<Void> deleteTests(@PathVariable(name = "classId") final Integer classId,
											@PathVariable(name = "testId") final Integer testId)
	{
		testClassService.delete(classId, testId);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{classId}/files")
	@ApiResponse(responseCode = "201")
	public ResponseEntity<Integer> addFileToClass(@PathVariable(name = "classId") final Integer classId,
											   @RequestBody @Valid final ClassFileRequest classFileRequest)
	{
		final Integer fileId = classFileService.create(classId, classFileRequest);
		return new ResponseEntity<>(fileId, HttpStatus.CREATED);
	}

	@GetMapping("/{classId}/files/get-all")
	@ApiResponse(responseCode = "201")
	public ResponseEntity<List<ClassFileResponse>> getAllFiles(@PathVariable(name = "classId") final Integer classId)
	{
		return ResponseEntity.ok(classFileService.findAllByClass(classId));
	}


	@DeleteMapping("/{classId}/files/{fileId}")
	@ApiResponse(responseCode = "204")
	public ResponseEntity<Void> deleteFile(@PathVariable(name = "classId") final Integer classId,
											@PathVariable(name = "fileId") final Integer fileId)
	{
		classFileService.delete(classId, fileId);
		return ResponseEntity.noContent().build();
	}
}
