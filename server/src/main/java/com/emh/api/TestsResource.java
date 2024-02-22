package com.emh.api;

import com.emh.model.TestsDTO;
import com.emh.service.TestsService;
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
@RequestMapping(value = "/api/testss", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestsResource {

    private final TestsService testsService;

    public TestsResource(final TestsService testsService) {
        this.testsService = testsService;
    }

    @GetMapping
    public ResponseEntity<List<TestsDTO>> getAllTestss() {
        return ResponseEntity.ok(testsService.findAll());
    }

    @GetMapping("/{testCode}")
    public ResponseEntity<TestsDTO> getTests(
            @PathVariable(name = "testCode") final Integer testCode) {
        return ResponseEntity.ok(testsService.get(testCode));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createTests(@RequestBody @Valid final TestsDTO testsDTO) {
        final Integer createdTestCode = testsService.create(testsDTO);
        return new ResponseEntity<>(createdTestCode, HttpStatus.CREATED);
    }

    @PutMapping("/{testCode}")
    public ResponseEntity<Integer> updateTests(
            @PathVariable(name = "testCode") final Integer testCode,
            @RequestBody @Valid final TestsDTO testsDTO) {
        testsService.update(testCode, testsDTO);
        return ResponseEntity.ok(testCode);
    }

    @DeleteMapping("/{testCode}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTests(
            @PathVariable(name = "testCode") final Integer testCode) {
        final ReferencedWarning referencedWarning = testsService.getReferencedWarning(testCode);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        testsService.delete(testCode);
        return ResponseEntity.noContent().build();
    }

}
