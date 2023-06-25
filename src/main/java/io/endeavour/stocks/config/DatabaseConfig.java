package io.endeavour.stocks.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


@Configuration
public class DatabaseConfig {

   @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource()
   {
       return DataSourceBuilder.create().build();
   }

   @Bean (name = "dataSourceCrud")
    @ConfigurationProperties (prefix = "spring.datasource-crudjpa")
    public DataSource dataSourceCrud()
   {
       return DataSourceBuilder.create().build();
   }

}
