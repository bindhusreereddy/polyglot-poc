package com.example.reconciliation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class SpringBootSQLiteApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSQLiteApp.class, args);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:/path/to/your/database.db"); // Replace with your SQLite DB path
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @RestController
    public class SQLiteController {

        private final JdbcTemplate jdbcTemplate;

        public SQLiteController(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        @GetMapping("/records")
        public List<Map<String, Object>> getRecords() {
            // Assumes a table named 'users' with columns 'id' and 'name'
            return jdbcTemplate.queryForList("SELECT * FROM users");
        }
    }
}