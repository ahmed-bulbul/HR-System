
-- V3__create_department_table.sql
CREATE TABLE departments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    department_name VARCHAR(255) NOT NULL,
    department_code VARCHAR(255) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    tenant_id VARCHAR(100),

    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    version BIGINT DEFAULT 0,
    is_active BOOLEAN DEFAULT true
);
