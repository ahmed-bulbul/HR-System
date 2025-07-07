package com.hrsystem.employee.dto;

import com.hrsystem.employee.enums.DepartmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO extends BaseDTO {

    private String departmentName;
    private String departmentCode;
    private String description;
    private DepartmentStatus status;
}
