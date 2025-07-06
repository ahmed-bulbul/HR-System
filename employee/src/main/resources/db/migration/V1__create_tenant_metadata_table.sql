-- Create tenant_metadata table in public schema to track all tenants
CREATE TABLE IF NOT EXISTS public.tenant_metadata (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL UNIQUE,
    schema_name VARCHAR(100) NOT NULL UNIQUE,
    company_name VARCHAR(255),
    subscription_plan VARCHAR(50),
    max_employees INTEGER,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now()
);

-- Optional: Add an index for faster lookups
CREATE INDEX IF NOT EXISTS idx_tenant_schema ON public.tenant_metadata(schema_name);

-- Insert tenants only if they don't already exist
INSERT INTO public.tenant_metadata (tenant_id, schema_name, company_name, subscription_plan, max_employees)
SELECT *
FROM (
    VALUES
        ('tenant1', 'tenant1', 'Tenant 1', 'Basic', 1000),
        ('tenant2', 'tenant2', 'Tenant 2', 'Premium', 5000)
) AS t(tenant_id, schema_name, company_name, subscription_plan, max_employees)
WHERE NOT EXISTS (
    SELECT 1 FROM public.tenant_metadata tm WHERE tm.tenant_id = t.tenant_id
);


-- V1__create_audit_log_table.sql
CREATE TABLE IF NOT EXISTS audit_logs (
    id SERIAL PRIMARY KEY,
    entity_name VARCHAR(255),
    entity_id VARCHAR(255),
    tenant_id VARCHAR(100),
    action VARCHAR(50),
    description TEXT,
    timestamp TIMESTAMP,
    performed_by VARCHAR(255)
);
