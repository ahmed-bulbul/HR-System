package com.hrsystem.employee.config;

import jakarta.annotation.PostConstruct;
import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class FlywayMigrationConfig {

    private final DataSource dataSource;

    public FlywayMigrationConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void migrateAllSchemas() {
        // STEP 1: Migrate public schema
        migrateSchema("public");

        // STEP 2: Read all tenant schemas from public.tenant_metadata
        List<String> tenantSchemas = fetchAllSchemasFromTenantMetadata();

        // STEP 3: Apply same migration to each tenant schema
        for (String schema : tenantSchemas) {
            createSchemaIfNotExists(schema); // ensure schema exists
            migrateSchema(schema);           // run same migration scripts
        }
    }

    private void migrateSchema(String schema) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .schemas(schema)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();

        flyway.migrate();
    }

    private void createSchemaIfNotExists(String schema) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE SCHEMA IF NOT EXISTS " + schema);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create schema: " + schema, e);
        }
    }

    private List<String> fetchAllSchemasFromTenantMetadata() {
        List<String> schemas = new ArrayList<>();
        String sql = "SELECT schema_name FROM public.tenant_metadata";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                schemas.add(rs.getString("schema_name"));
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("tenant_metadata")) {
                System.out.println("tenant_metadata not found yet. Skipping tenants.");
            } else {
                throw new RuntimeException("Error fetching tenant schemas", e);
            }
        }
        return schemas;
    }
}
