package com.hrsystem.employee.controller;


import com.hrsystem.employee.dto.DepartmentCreateDto;
import com.hrsystem.employee.dto.DepartmentDTO;
import com.hrsystem.employee.response.ApiResponse;
import com.hrsystem.employee.response.PageResponse;
import com.hrsystem.employee.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService service;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create( @RequestBody DepartmentCreateDto createDTO) {
        DepartmentDTO created = service.create(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Created", created.getId()));

    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<DepartmentDTO>>> getAllDepartments(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "search", required = false) String search) {

        Pageable pageable = PageRequest.of(page, size);
        PageResponse<DepartmentDTO> departments = service.findAll(search, pageable);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "Success", departments)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentDTO>> getDepartmentById(@PathVariable Long id) {
        DepartmentDTO department = service.findById(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Success", department));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> update(@PathVariable Long id, @Valid @RequestBody DepartmentDTO updateDTO) {
        DepartmentDTO updated = service.update(id, updateDTO);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Success", updated.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> delete(@PathVariable Long id) {
        boolean deleted = service.delete(id);
        return ResponseEntity.noContent().build();
    }





}
