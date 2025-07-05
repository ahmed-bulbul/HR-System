package com.hrsystem.employee.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class MultiTenantDataSourceConfig {

    @Bean
    public DataSource dataSource(DataSourceProperties properties) {
        DataSource baseDataSource = DataSourceBuilder.create()
                .url(properties.getUrl())
                .username(properties.getUsername())
                .password(properties.getPassword())
                .driverClassName(properties.getDriverClassName())
                .build();

        return new SchemaAwareDataSource(baseDataSource);
    }
}
