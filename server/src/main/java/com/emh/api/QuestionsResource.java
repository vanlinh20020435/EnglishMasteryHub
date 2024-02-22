package com.emh.api;

import com.emh.model.QuestionsDTO;
import com.emh.service.QuestionsService;
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
@RequestMapping(value = "/api/questionss", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestionsResource {

    private final QuestionsService questionsService;

    public QuestionsResource(final QuestionsService questionsService) {
        this.questionsService = questionsService;
    }

    @GetMapping
    public ResponseEntity<List<QuestionsDTO>> getAllQuestionss() {
        return ResponseEntity.ok(questionsService.findAll());
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionsDTO> getQuestions(
            @PathVariable(name = "questionId") final Integer questionId) {
        return ResponseEntity.ok(questionsService.get(questionId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createQuestions(
            @RequestBody @Valid final QuestionsDTO questionsDTO) {
        final Integer createdQuestionId = questionsService.create(questionsDTO);
        return new ResponseEntity<>(createdQuestionId, HttpStatus.CREATED);
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<Integer> updateQuestions(
            @PathVariable(name = "questionId") final Integer questionId,
            @RequestBody @Valid final QuestionsDTO questionsDTO) {
        questionsService.update(questionId, questionsDTO);
        return ResponseEntity.ok(questionId);
    }

    @DeleteMapping("/{questionId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteQuestions(
            @PathVariable(name = "questionId") final Integer questionId) {
        final ReferencedWarning referencedWarning = questionsService.getReferencedWarning(questionId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        questionsService.delete(questionId);
        return ResponseEntity.noContent().build();
    }

}
