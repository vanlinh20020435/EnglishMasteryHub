package com.emh.api;

import com.emh.payload.request.StudentTestResultRequest;
import com.emh.payload.response.StudentDetailSummaryResponse;
import com.emh.payload.response.StudentSummaryResponse;
import com.emh.payload.response.StudentTestResultResponse;
import com.emh.payload.response.TestResultStatisticResponse;
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
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"})
	public ResponseEntity<List<StudentTestResultResponse>> getAllResults(@RequestParam(required = true) final Integer classId,
																		 @RequestParam(required = true) final Integer testId,
																		 @RequestParam(required = false) final Integer studentId) throws Exception
	{
		return ResponseEntity.ok(testResultService.findAll(classId, testId, studentId));
	}

	@GetMapping("/statistic")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"})
	public ResponseEntity<TestResultStatisticResponse> statistic(@RequestParam(required = true) final Integer classId,
																 @RequestParam(required = true) final Integer testId) throws Exception
	{
		return ResponseEntity.ok(testResultService.statistic(classId, testId));
	}

	@GetMapping("/statistic/{studentId}")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"})
	public ResponseEntity<List<StudentDetailSummaryResponse>> statistic(@RequestParam(required = true) final Integer classId,
																		@RequestParam(required = true) final Integer testId,
																		@PathVariable(name = "studentId") final Integer studentId) throws Exception
	{
		return ResponseEntity.ok(testResultService.statistic(classId, testId, studentId));
	}

	@GetMapping("/{resultId}")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"})
	public ResponseEntity<StudentTestResultResponse> getResults(
			@PathVariable(name = "resultId") final Integer resultId)
	{
		return ResponseEntity.ok(testResultService.get(resultId));
	}

	@PostMapping
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"})
	public ResponseEntity<Integer> createResults(@RequestBody @Valid final StudentTestResultRequest testResultRequest) throws IOException
	{
		final Integer testId = testResultService.create(testResultRequest);
		return new ResponseEntity<>(testId, HttpStatus.CREATED);
	}

	@PutMapping("/{resultId}")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
	public ResponseEntity<Integer> updateResults(
			@PathVariable(name = "resultId") final Integer resultId,
			@RequestBody @Valid final StudentTestResultRequest testResultRequest) throws IOException
	{
		testResultService.update(resultId, testResultRequest);
		return ResponseEntity.ok(resultId);
	}
}
