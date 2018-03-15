package com.agonyengine.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {
    @Bean
    @ConfigurationProperties(prefix="agony.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
}
