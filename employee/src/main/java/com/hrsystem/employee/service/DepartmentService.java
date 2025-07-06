package com.hrsystem.employee.service;

import com.hrsystem.employee.audit.Auditable;
import com.hrsystem.employee.dto.DepartmentCreateDto;
import com.hrsystem.employee.dto.DepartmentDTO;
import com.hrsystem.employee.mapper.DepartmentMapper;
import com.hrsystem.employee.model.Department;
import com.hrsystem.employee.repository.DepartmentRepository;
import com.hrsystem.employee.util.AuditLoggerUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Optional<DepartmentDTO> findById(UUID id) {
        return repository.findById(id).map(DepartmentMapper.TO_DTO);
    }

    @Auditable(entity = "Department", action = "UPDATE", description = "Updated a department")
    public Optional<DepartmentDTO> update(UUID id, DepartmentDTO updateDTO) {
        return repository.findById(id)
                .map(department -> DepartmentMapper.UPDATE_ENTITY.apply(department, updateDTO))
                .map(repository::save)
                .map(DepartmentMapper.TO_DTO);
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

    public Page<DepartmentDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(DepartmentMapper.TO_DTO);
    }
}
