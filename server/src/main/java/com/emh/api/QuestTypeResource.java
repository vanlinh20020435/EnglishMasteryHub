package com.emh.api;

import com.emh.model.QuestTypeDTO;
import com.emh.service.QuestTypeService;
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
@RequestMapping(value = "/api/questTypes", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestTypeResource {

    private final QuestTypeService questTypeService;

    public QuestTypeResource(final QuestTypeService questTypeService) {
        this.questTypeService = questTypeService;
    }

    @GetMapping
    public ResponseEntity<List<QuestTypeDTO>> getAllQuestTypes() {
        return ResponseEntity.ok(questTypeService.findAll());
    }

    @GetMapping("/{typeId}")
    public ResponseEntity<QuestTypeDTO> getQuestType(
            @PathVariable(name = "typeId") final Integer typeId) {
        return ResponseEntity.ok(questTypeService.get(typeId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createQuestType(
            @RequestBody @Valid final QuestTypeDTO questTypeDTO) {
        final Integer createdTypeId = questTypeService.create(questTypeDTO);
        return new ResponseEntity<>(createdTypeId, HttpStatus.CREATED);
    }

    @PutMapping("/{typeId}")
    public ResponseEntity<Integer> updateQuestType(
            @PathVariable(name = "typeId") final Integer typeId,
            @RequestBody @Valid final QuestTypeDTO questTypeDTO) {
        questTypeService.update(typeId, questTypeDTO);
        return ResponseEntity.ok(typeId);
    }

    @DeleteMapping("/{typeId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteQuestType(
            @PathVariable(name = "typeId") final Integer typeId) {
        final ReferencedWarning referencedWarning = questTypeService.getReferencedWarning(typeId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        questTypeService.delete(typeId);
        return ResponseEntity.noContent().build();
    }

}
