package com.hrsystem.employee.dto;

import com.hrsystem.employee.enums.DepartmentStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentCreateDto {
    private String departmentName;
    private String description;
    private DepartmentStatus status;
    @NotBlank(message = "Department code is required")
    private String departmentCode;
}
