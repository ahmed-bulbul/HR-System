package com.hrsystem.employee.dto;

import com.hrsystem.employee.enums.DepartmentStatus;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class DepartmentDTO extends BaseDTO {

    private String departmentName;
    private String departmentCode;
    private String description;
    private DepartmentStatus status;
}
