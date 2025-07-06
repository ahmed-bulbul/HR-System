package com.hrsystem.employee.service;

import com.hrsystem.employee.audit.Auditable;
import com.hrsystem.employee.dto.DepartmentCreateDto;
import com.hrsystem.employee.dto.DepartmentDTO;
import com.hrsystem.employee.exception.DataNotFoundException;
import com.hrsystem.employee.mapper.DepartmentMapper;
import com.hrsystem.employee.model.Department;
import com.hrsystem.employee.repository.DepartmentRepository;
import com.hrsystem.employee.response.PageResponse;
import com.hrsystem.employee.util.AuditLoggerUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentService {

    private final DepartmentRepository repository;
    private final AuditLoggerUtil auditLoggerUtil;

    @Auditable(entity = "Department", action = "CREATE", description = "Created a new department")
    public DepartmentDTO create(DepartmentCreateDto createDTO) {
        Department department = DepartmentMapper.TO_ENTITY.apply(createDTO);
        department = repository.save(department);
        return DepartmentMapper.TO_DTO.apply(department);
    }

    public DepartmentDTO findById(UUID id) {
        return repository.findById(id).map(DepartmentMapper.TO_DTO)
                .orElseThrow(() -> new DataNotFoundException("Department not found"));
    }

    @Auditable(entity = "Department", action = "UPDATE", description = "Updated a department")
    public DepartmentDTO update(UUID id, DepartmentDTO updateDTO) {
        Department department = repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Department not found"));

        Department updated = DepartmentMapper.UPDATE_ENTITY.apply(department, updateDTO);
        Department saved = repository.save(updated);

        return DepartmentMapper.TO_DTO.apply(saved);
    }


    @Auditable(entity = "Department", action = "DELETE", description = "Deleted a department")
    public boolean delete(UUID id) {
        return repository.findById(id)
                .map(department -> {
                    repository.delete(department);
                    return true;
                })
                .orElse(false);
    }

    public PageResponse<DepartmentDTO> findAll(String search,Pageable pageable) {

        Page<Department> departments;

        if (search == null || search.isBlank()) {
            departments = repository.findAll(pageable);
        } else {
            departments = repository.searchByMultipleFields(search, pageable);
        }

        List<DepartmentDTO> data = departments.getContent()
                .stream()
                .map(DepartmentMapper.TO_DTO)
                .toList();

        return new PageResponse<>(
                data,
                pageable.getPageNumber(),    // pageNumber
                pageable.getPageSize(),      // pageSize
                departments.getTotalElements(),  // totalElements
                departments.getTotalPages()       // totalPages
        );

    }

}
