-- Minimal schema compatible with the existing JDBC queries.
-- The original database DDL is not included in this project.
CREATE TABLE IF NOT EXISTS students (
    id SERIAL PRIMARY KEY,
    name TEXT,
    marks DOUBLE PRECISION
);
