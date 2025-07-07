package com.hrsystem.employee.mapper;


import com.hrsystem.employee.dto.DepartmentCreateDto;
import com.hrsystem.employee.dto.DepartmentDTO;
import com.hrsystem.employee.model.Department;
import com.hrsystem.employee.util.BaseMapperUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

@Component
public class DepartmentMapper {

    public static final Function<Department, DepartmentDTO> TO_DTO = department -> {

        DepartmentDTO dto = DepartmentDTO.builder()
                .departmentName(department.getDepartmentName())
                .description(department.getDescription())
                .status(department.getStatus())
                .departmentCode(department.getDepartmentCode())
                .build();

        return BaseMapperUtil.mapBaseFields(department, dto);

    };

    public static final Function<DepartmentCreateDto, Department> TO_ENTITY = dto ->
            Department.builder()
            .departmentName(dto.getDepartmentName())
            .description(dto.getDescription())
            .status(dto.getStatus())
            .departmentCode(dto.getDepartmentCode())
            .build();

    //update
    public static final BiFunction<Department, DepartmentDTO, Department> UPDATE_ENTITY = (department, dto) -> {

        department.setDepartmentName(dto.getDepartmentName());
        department.setDescription(dto.getDescription());
        department.setStatus(dto.getStatus());
        department.setDepartmentCode(dto.getDepartmentCode());
        return department;
    };

    //Batch Mapping
    public static final Function<List<Department>, List<DepartmentDTO>> TO_DTO_LIST = departments ->
            departments.stream().map(TO_DTO).toList();


    public static final Function<Page<Department>, Page<DepartmentDTO>> TO_DTO_PAGE =
            productPage -> productPage.map(TO_DTO);

}
