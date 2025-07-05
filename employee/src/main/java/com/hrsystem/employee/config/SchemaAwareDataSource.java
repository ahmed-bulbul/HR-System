package com.hrsystem.employee.config;

import com.hrsystem.employee.contexts.TenantContext;
import org.springframework.jdbc.datasource.DelegatingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaAwareDataSource extends DelegatingDataSource {
    public SchemaAwareDataSource(DataSource targetDataSource) {
        super(targetDataSource);
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = super.getConnection();
        String schema = TenantContext.getTenantId();
        if (schema != null) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("SET search_path TO " + schema);
            }
        }
        return conn;
    }
}
