package br.lms.bigchatbrasil.infrastructure.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DBConfig {

    @Value("${POSTGRES_URL}")
    private String dbUrl;

    @Value("${POSTGRES_USER}")
    private String dbUsername;

    @Value("${POSTGRES_PASSWORD}")
    private String dbPassword;

    @Bean
    public DataSource dataSource() {
        String postgresName = "org.postgresql.Driver";
        return DataSourceBuilder
                .create()
                .driverClassName(postgresName)
                .url(dbUrl)
                .username(dbUsername)
                .password(dbPassword)
                .build();
    }
}