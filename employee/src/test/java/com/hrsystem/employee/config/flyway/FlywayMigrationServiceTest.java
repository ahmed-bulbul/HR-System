package com.hrsystem.employee.config.flyway;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class FlywayMigrationServiceTest {

    private DataSource mockDataSource;
    private Connection mockConnection;
    private Statement mockStatement;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    private FlywayMigrationService migrationConfig;

    @BeforeEach
    public void setup() throws Exception {
        mockDataSource = mock(DataSource.class);
        mockConnection = mock(Connection.class);
        mockStatement = mock(Statement.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        migrationConfig = new FlywayMigrationService(mockDataSource);
    }

    @Test
    public void testMigrateAllSchemas() throws Exception {
        // Mock public.tenant_metadata result
        when(mockConnection.prepareStatement("SELECT schema_name FROM public.tenant_metadata")).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // Simulate 2 tenant schemas in the result set
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString("schema_name")).thenReturn("tenant1", "tenant2");

        // Mock Flyway static calls and objects
        FluentConfiguration mockConfig = mock(FluentConfiguration.class);
        Flyway mockFlyway = mock(Flyway.class);

        try (MockedStatic<Flyway> flywayStatic = mockStatic(Flyway.class)) {
            flywayStatic.when(Flyway::configure).thenReturn(mockConfig);
            when(mockConfig.dataSource(mockDataSource)).thenReturn(mockConfig);
            when(mockConfig.schemas(any())).thenReturn(mockConfig);
            when(mockConfig.locations(any(String.class))).thenReturn(mockConfig);
            when(mockConfig.baselineOnMigrate(true)).thenReturn(mockConfig);
            when(mockConfig.load()).thenReturn(mockFlyway);

          //  doReturn(0).when(mockFlyway).migrate();



            // Call your method under test
            migrationConfig.migrateAllSchemas();

            // Verify schema creation executed for each tenant schema
            verify(mockStatement).execute("CREATE SCHEMA IF NOT EXISTS tenant1");
            verify(mockStatement).execute("CREATE SCHEMA IF NOT EXISTS tenant2");

            // Verify Flyway migrate() called 3 times: once for public + twice for tenants
            verify(mockFlyway, times(3)).migrate();
        }
    }
}
