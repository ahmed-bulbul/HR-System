package com.hrsystem.employee.config.datasource;

import com.hrsystem.employee.context.TenantContext;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.lang.NonNull;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaAwareDataSource extends DelegatingDataSource {
    public SchemaAwareDataSource(DataSource targetDataSource) {
        super(targetDataSource);
    }

    @Override
    @NonNull
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
