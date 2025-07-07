package com.hrsystem.employee.model;

import com.hrsystem.employee.enums.DepartmentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "departments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Department extends BaseEntity{

    @Column(name = "department_name", nullable = false)
    private String departmentName;

    @Column(name = "department_code", nullable = false,unique = true)
    private String departmentCode;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "status", nullable = false)
    private DepartmentStatus status;


}
