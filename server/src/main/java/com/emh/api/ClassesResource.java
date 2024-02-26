package com.emh.api;

import com.emh.payload.request.ClassesRequest;
import com.emh.payload.response.ClassesResponse;
import com.emh.payload.response.TestsResponse;
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

	public ClassesResource(final ClassesService classesService,
						   final TestClassService testClassService)
	{
		this.classesService = classesService;
		this.testClassService = testClassService;
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
		final ReferencedWarning referencedWarning = classesService.getReferencedWarning(classId);
		if (referencedWarning != null)
		{
			throw new ReferencedException(referencedWarning);
		}
		testClassService.delete(classId, testId);
		return ResponseEntity.noContent().build();
	}
}
