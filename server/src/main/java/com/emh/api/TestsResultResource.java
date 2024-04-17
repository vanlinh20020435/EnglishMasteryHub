package com.emh.api;

import com.emh.payload.request.StudentTestResultRequest;
import com.emh.payload.response.StudentTestResultResponse;
import com.emh.service.StudentTestResultService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/test-result", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestsResultResource
{

	private final StudentTestResultService testResultService;

	public TestsResultResource(final StudentTestResultService testResultService)
	{
		this.testResultService = testResultService;
	}

	@GetMapping
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
	public ResponseEntity<List<StudentTestResultResponse>> getAllTests()
	{
		return ResponseEntity.ok(testResultService.findAll());
	}

	@GetMapping("/{resultId}")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
	public ResponseEntity<StudentTestResultResponse> getTests(
			@PathVariable(name = "resultId") final Integer resultId)
	{
		return ResponseEntity.ok(testResultService.get(resultId));
	}

	@PostMapping
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
	public ResponseEntity<Integer> createTests(@RequestBody @Valid final StudentTestResultRequest testResultRequest) throws IOException
	{
		final Integer testId = testResultService.create(testResultRequest);
		return new ResponseEntity<>(testId, HttpStatus.CREATED);
	}
}
