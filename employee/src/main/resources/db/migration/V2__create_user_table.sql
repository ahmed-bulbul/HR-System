-- V2__create_user_table.sql

CREATE TABLE IF NOT EXISTS Employee (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now()
);

-- insert users only if they don't already exist
INSERT INTO Employee (first_name, last_name, email, password, role)
SELECT *
FROM (
    VALUES
        ('John', 'Doe', 'e1Z9l@example.com', '$2a$10$4HnYxOjgPp4h0WjH8jgPp4h0WjH8jgPp4h0WjH8jH8jH8jH8jH', 'ROLE_ADMIN'),
        ('Jane', 'Doe', 'm9t0R@example.com', '$2a$10$4HnYxOjgPp4h0WjH8jgPp4h0WjH8jgPp4h0WjH8jH8jH8jH8jH', 'ROLE_USER')
) AS u(first_name, last_name, email, password, role)
WHERE NOT EXISTS (
    SELECT 1 FROM Employee e WHERE e.email = u.email
);