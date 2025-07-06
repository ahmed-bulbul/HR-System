package com.hrsystem.employee.audit;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entityName;
    private String entityId;
    private String action; // CREATE, UPDATE, DELETE
    private String tenantId;

    @Column(length = 1000)
    private String description;

    private LocalDateTime timestamp;

    private String performedBy; // Optional: username, tenantId, etc.
}
