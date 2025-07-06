package com.hrsystem.employee.controller;


import com.hrsystem.employee.dto.DepartmentCreateDto;
import com.hrsystem.employee.dto.DepartmentDTO;
import com.hrsystem.employee.response.ApiResponse;
import com.hrsystem.employee.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<ApiResponse<UUID>> create(@Valid @RequestBody DepartmentCreateDto createDTO) {
        DepartmentDTO created = service.create(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Created", created.getId()));

    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DepartmentDTO>>> getAllDepartments(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<DepartmentDTO> departments = service.findAll(pageable);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "Success", departments)
        );
    }




}
