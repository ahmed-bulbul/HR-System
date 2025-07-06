package com.hrsystem.employee.audit;

import com.hrsystem.employee.context.TenantContext;
import com.hrsystem.employee.util.AuditLoggerUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditLoggingAspect {

    private final AuditLoggerUtil auditLoggerUtil;

    @AfterReturning(pointcut = "@annotation(com.hrsystem.employee.audit.Auditable)", returning = "result")
    public void logAudit(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Auditable auditable = signature.getMethod().getAnnotation(Auditable.class);

        if (auditable == null) {
            return;
        }

        String entityName = auditable.entity();
        String action = auditable.action();
        String description = auditable.description();

        // Example: get entity id or name from the returned DTO or method arguments
        String entityId = "N/A";

        if (result != null) {
            try {
                // Assuming returned object has a getId() method or getEntityName() method
                Object id = result.getClass().getMethod("getId").invoke(result);
                if (id != null) entityId = id.toString();
            } catch (Exception e) {
                // fallback or ignore
            }
        }


        auditLoggerUtil.log(entityName, entityId, action, description);
    }
}
