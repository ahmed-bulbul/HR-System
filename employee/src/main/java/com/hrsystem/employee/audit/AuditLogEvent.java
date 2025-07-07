package com.hrsystem.employee.audit;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuditLogEvent {
    private String entityName;
    private Long entityId;
    private Long tenantId;
    private String action;
    private String description;
    private String performedBy;
    private LocalDateTime timestamp;
}
