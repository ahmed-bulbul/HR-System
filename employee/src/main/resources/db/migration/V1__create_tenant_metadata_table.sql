-- 1. Create tenant_metadata table in public schema
CREATE TABLE IF NOT EXISTS public.tenant_metadata (
    id BIGSERIAL PRIMARY KEY,
    schema_name VARCHAR(100) NOT NULL UNIQUE,
    company_name VARCHAR(255),
    subscription_plan VARCHAR(50),
    max_employees INTEGER,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now()
);

-- Optional: Add an index for faster lookups
CREATE INDEX IF NOT EXISTS idx_tenant_schema ON public.tenant_metadata(schema_name);

-- 2. Insert tenants only if they don't already exist
INSERT INTO public.tenant_metadata (schema_name, company_name, subscription_plan, max_employees)
SELECT t.schema_name, t.company_name, t.subscription_plan, t.max_employees
FROM (
    VALUES
        ('tenant1', 'Tenant 1', 'Basic', 1000),
        ('tenant2', 'Tenant 2', 'Premium', 5000)
) AS t(schema_name, company_name, subscription_plan, max_employees)
WHERE NOT EXISTS (
    SELECT 1 FROM public.tenant_metadata e WHERE e.schema_name = t.schema_name
);



CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGSERIAL PRIMARY KEY,
    entity_name VARCHAR(255) NOT NULL,
    entity_id BIGINT NOT NULL,
    action VARCHAR(50) NOT NULL,
    description TEXT,
    timestamp TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    performed_by VARCHAR(255)
);



CREATE TABLE IF NOT EXISTS departments (
    id BIGSERIAL PRIMARY KEY,
    department_name VARCHAR(255) NOT NULL,
    department_code VARCHAR(255) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,

    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    version BIGINT DEFAULT 0,
    is_active BOOLEAN DEFAULT true
);


