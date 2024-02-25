package com.emh.api;

import com.emh.payload.request.TestsRequest;
import com.emh.payload.response.TestsResponse;
import com.emh.service.TestsService;
import com.emh.util.ReferencedException;
import com.emh.util.ReferencedWarning;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/testss", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestsResource
{

	private final TestsService testsService;

	public TestsResource(final TestsService testsService)
	{
		this.testsService = testsService;
	}

	@GetMapping
	public ResponseEntity<List<TestsResponse>> getAllTestss()
	{
		return ResponseEntity.ok(testsService.findAll());
	}

	@GetMapping("/{testId}")
	public ResponseEntity<TestsResponse> getTests(
			@PathVariable(name = "testId") final Integer testId)
	{
		return ResponseEntity.ok(testsService.get(testId));
	}

	@PostMapping
	public ResponseEntity<Integer> createTests(@RequestBody @Valid final TestsRequest testsDTO)
	{
		final Integer createdTestCode = testsService.create(testsDTO);
		return new ResponseEntity<>(createdTestCode, HttpStatus.CREATED);
	}

	@PutMapping("/{testId}")
	public ResponseEntity<Integer> updateTests(
			@PathVariable(name = "testId") final Integer testId,
			@RequestBody @Valid final TestsRequest testsDTO)
	{
		testsService.update(testId, testsDTO);
		return ResponseEntity.ok(testId);
	}

	@DeleteMapping("/{testId}")
	public ResponseEntity<Void> deleteTests(
			@PathVariable(name = "testId") final Integer testId)
	{
		final ReferencedWarning referencedWarning = testsService.getReferencedWarning(testId);
		if (referencedWarning != null)
		{
			throw new ReferencedException(referencedWarning);
		}
		testsService.delete(testId);
		return ResponseEntity.noContent().build();
	}

}
