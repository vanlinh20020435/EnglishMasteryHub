package com.emh.api;

import com.emh.model.AdminDTO;
import com.emh.service.AdminService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/admins", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminResource {

    private final AdminService adminService;

    public AdminResource(final AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public ResponseEntity<List<AdminDTO>> getAllAdmins() {
        return ResponseEntity.ok(adminService.findAll());
    }

    @GetMapping("/{adminId}")
    public ResponseEntity<AdminDTO> getAdmin(
            @PathVariable(name = "adminId") final Integer adminId) {
        return ResponseEntity.ok(adminService.get(adminId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createAdmin(@RequestBody @Valid final AdminDTO adminDTO) {
        final Integer createdAdminId = adminService.create(adminDTO);
        return new ResponseEntity<>(createdAdminId, HttpStatus.CREATED);
    }

    @PutMapping("/{adminId}")
    public ResponseEntity<Integer> updateAdmin(
            @PathVariable(name = "adminId") final Integer adminId,
            @RequestBody @Valid final AdminDTO adminDTO) {
        adminService.update(adminId, adminDTO);
        return ResponseEntity.ok(adminId);
    }

    @DeleteMapping("/{adminId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAdmin(@PathVariable(name = "adminId") final Integer adminId) {
        adminService.delete(adminId);
        return ResponseEntity.noContent().build();
    }

}
