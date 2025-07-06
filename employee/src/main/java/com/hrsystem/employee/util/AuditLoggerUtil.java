package com.hrsystem.employee.util;

import com.hrsystem.employee.audit.AuditLogEvent;
import com.hrsystem.employee.context.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuditLoggerUtil {

    private final ApplicationEventPublisher eventPublisher;

    public void log(String entityName, Object entityId, String action, String description) {
        AuditLogEvent event = AuditLogEvent.builder()
                .entityName(entityName)
                .entityId(entityId.toString())
                .tenantId(getTenantId())
                .action(action)
                .description(description)
                .performedBy(getPerformedBy())
                .timestamp(LocalDateTime.now())
                .build();

        eventPublisher.publishEvent(event);
    }

    private String getPerformedBy() {
        //from security context or from token
        return "system";

    }

    private String getTenantId() {
        //get tenant id from context
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            return "0";
        }
        return tenantId;
    }
}
