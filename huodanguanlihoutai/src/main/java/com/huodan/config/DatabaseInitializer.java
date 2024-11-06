package com.huodan.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DatabaseInitializer {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public DatabaseInitializer(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void initialize() {
        String baseUrl = url.substring(0, url.indexOf("?"));
        String dbName = baseUrl.substring(baseUrl.lastIndexOf("/") + 1);
        String mysqlUrl = baseUrl.substring(0, baseUrl.lastIndexOf("/"));

        try (Connection conn = DriverManager.getConnection(mysqlUrl, username, password)) {
            // 检查数据库是否存在
            try {
                conn.createStatement().executeQuery("USE " + dbName);
                // 数据库存在，检查是否有用户表
                try {
                    jdbcTemplate.queryForObject("SELECT COUNT(*) FROM sys_user", Integer.class);
                } catch (Exception e) {
                    // 用户表不存在，执行初始化脚本
                    executeSchema();
                }
            } catch (SQLException e) {
                // 数据库不存在，创建数据库并执行初始化脚本
                conn.createStatement().execute("CREATE DATABASE IF NOT EXISTS " + dbName + " DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci");
                executeSchema();
            }
        } catch (SQLException e) {
            throw new RuntimeException("数据库初始化失败", e);
        }
    }

    private void executeSchema() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("db/schema.sql"));
        populator.setSqlScriptEncoding("UTF-8");
        populator.execute(dataSource);
    }
} 