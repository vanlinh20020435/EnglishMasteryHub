package com.emh.api;

import com.emh.model.ClassesDTO;
import com.emh.service.ClassesService;
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
public class ClassesResource {

    private final ClassesService classesService;

    public ClassesResource(final ClassesService classesService) {
        this.classesService = classesService;
    }

    @GetMapping
    public ResponseEntity<List<ClassesDTO>> getAllClassess() {
        return ResponseEntity.ok(classesService.findAll());
    }

    @GetMapping("/{classId}")
    public ResponseEntity<ClassesDTO> getClasses(
            @PathVariable(name = "classId") final Integer classId) {
        return ResponseEntity.ok(classesService.get(classId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createClasses(@RequestBody @Valid final ClassesDTO classesDTO) {
        final Integer createdClassId = classesService.create(classesDTO);
        return new ResponseEntity<>(createdClassId, HttpStatus.CREATED);
    }

    @PutMapping("/{classId}")
    public ResponseEntity<Integer> updateClasses(
            @PathVariable(name = "classId") final Integer classId,
            @RequestBody @Valid final ClassesDTO classesDTO) {
        classesService.update(classId, classesDTO);
        return ResponseEntity.ok(classId);
    }

    @DeleteMapping("/{classId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteClasses(
            @PathVariable(name = "classId") final Integer classId) {
        final ReferencedWarning referencedWarning = classesService.getReferencedWarning(classId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        classesService.delete(classId);
        return ResponseEntity.noContent().build();
    }

}
