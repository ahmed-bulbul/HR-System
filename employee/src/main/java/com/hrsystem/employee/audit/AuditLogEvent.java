package com.hrsystem.employee.audit;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuditLogEvent {
    private String entityName;
    private String entityId;
    private String tenantId;
    private String action;
    private String description;
    private String performedBy;
    private LocalDateTime timestamp;
}
