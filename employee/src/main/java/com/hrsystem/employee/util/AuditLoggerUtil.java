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

    public void log(String entityName, Long entityId, String action, String description) {
        AuditLogEvent event = AuditLogEvent.builder()
                .entityName(entityName)
                .entityId(entityId)
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

    public static Long getTenantId() {
        // Get tenant ID from context
        String tenantIdStr = TenantContext.getTenantId();
        if (tenantIdStr == null) {
            return 0L;
        }
        try {
            return Long.valueOf(tenantIdStr);
        } catch (NumberFormatException e) {
            // Handle invalid tenant ID format
            return 0L;
        }
    }

}
