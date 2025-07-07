package com.hrsystem.employee.audit;


import com.hrsystem.employee.context.TenantContext;
import com.hrsystem.employee.util.AuditLoggerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuditLogListener {

    private final AuditLogRepository auditLogRepository;

    @EventListener
    public void onAuditEvent(AuditLogEvent event) {
        AuditLog log = AuditLog.builder()
                .entityName(event.getEntityName())
                .entityId(event.getEntityId())
                .tenantId(AuditLoggerUtil.getTenantId())
                .action(event.getAction())
                .description(event.getDescription())
                .performedBy(event.getPerformedBy())
                .timestamp(event.getTimestamp())
                .build();

        auditLogRepository.save(log);
    }


}

