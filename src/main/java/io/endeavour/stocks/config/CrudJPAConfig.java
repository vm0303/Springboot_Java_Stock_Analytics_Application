package io.endeavour.stocks.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories
        (
                basePackages = {"io.endeavour.stocks.repository.crud"},
                entityManagerFactoryRef = "crudEntityManagerFactory",
                transactionManagerRef = "crudTransactionManager"
        )
@EntityScan(basePackages = {
       "io.endeavour.stocks.entity.crud"
})



public class CrudJPAConfig
{
    @Autowired
    DataSource dataSourceCrud;

    @Bean(name="crudEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean crudEntityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSourceCrud);
        emf.setPackagesToScan("io.endeavour.stocks.entity.crud");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.ORACLE);
        vendorAdapter.setShowSql(true);

        emf.setJpaVendorAdapter(vendorAdapter);

        return emf;
    }

    @Bean(name = "crudTransactionManager")
    public PlatformTransactionManager crudTransactionManager (@Qualifier("crudEntityManagerFactory") EntityManagerFactory entityManagerFactory)
    {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }
}
