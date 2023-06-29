package io.endeavour.stocks.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;




@Configuration
@EnableJpaRepositories(
        basePackages = {"io.endeavour.stocks.repository.stocks"},
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
@EntityScan (basePackages = "io.endeavour.stocks.entity.stocks")
public class StocksJDBCJPAConfig
{
    @Autowired
    DataSource dataSource;


    @Bean
    public JdbcTemplate getJDBCTemplate()
    {
        return  new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate()
    {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    /**
     * This method is used to configure JPA with the following steps:
     * 1) Create an EntityManagerFactoryBean and tie it to the dataSource
     * 2) Direct the EntityManagerFactoryBean to the package where the entity classes are present
     * 3) Define an JPA implementation library that implementers the specs, which is Hibernate in our case
     * 4) Provide the database vendor information to the JPA Implementation library, which is Oracle in our case
     * 5) Set the JPA implementation vendor object into the EntityManagerFactoryBean
     * @return EntityManagerFactoryBean
     */
    @Bean(name = "entityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("io.endeavour.stocks.entity.stocks");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.ORACLE);
        vendorAdapter.setShowSql(true);

        emf.setJpaVendorAdapter(vendorAdapter);
        return emf;
    }

    @Bean (name = "transactionManager")
    public PlatformTransactionManager transactionManager( @Qualifier (value ="entityManagerFactory") EntityManagerFactory entityManagerFactory)
    {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }

}
