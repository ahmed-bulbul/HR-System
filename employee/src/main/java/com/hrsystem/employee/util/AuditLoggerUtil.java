package com.hrsystem.employee.util;

import com.hrsystem.employee.audit.AuditLogEvent;
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
                .action(action)
                .description(description)
                .performedBy(getPerformedBy())
                .timestamp(LocalDateTime.now())
                .build();

        eventPublisher.publishEvent(event);
    }

    public static String getPerformedBy() {
        //from security context or from token
        return "system";

    }


}
