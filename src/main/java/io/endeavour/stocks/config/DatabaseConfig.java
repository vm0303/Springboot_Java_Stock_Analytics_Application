package io.endeavour.stocks.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


@Configuration
public class DatabaseConfig {

    @Autowired
    private DataSource dataSource;


    /**
     * Method to return a JDBC template for database connection
     * @return JDBCTemplate
     */
    @Bean(name = "jdbcTemplate")
    public JdbcTemplate getJdbcTemplate()
    {
        return new JdbcTemplate(dataSource);
    }

}
