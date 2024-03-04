package br.lms.bigchatbrasil.infrastructure.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class DBConfig {

    @Value("${POSTGRES_URL}")
    private String dbUrl;

    @Value("${POSTGRES_USER}")
    private String dbUsername;

    @Value("${POSTGRES_PASSWORD}")
    private String dbPassword;

    @Profile("default")
    @Bean
    public DataSource prodDataSource() {
        return DataSourceBuilder
                .create()
                .driverClassName("org.postgresql.Driver")
                .url(dbUrl)
                .username(dbUsername)
                .password(dbPassword)
                .build();
    }

    @Profile({"dev", "test"})
    @Bean
    public DataSource devDataSource() {
        return DataSourceBuilder
                .create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
                .username("sa")
                .password("")
                .build();
    }
}