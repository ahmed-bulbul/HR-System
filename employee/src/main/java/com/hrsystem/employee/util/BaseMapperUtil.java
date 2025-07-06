package com.hrsystem.employee.util;

import com.hrsystem.employee.dto.BaseDTO;
import com.hrsystem.employee.model.BaseEntity;

public class BaseMapperUtil {

    public static <E extends BaseEntity, D extends BaseDTO> D mapBaseFields(E entity, D dto) {
        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setVersion(entity.getVersion());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }
}

