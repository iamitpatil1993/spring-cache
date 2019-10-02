package com.example.spring.cahce.configuration.data.jpa;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * @author amit
 */
@Configuration
@PropertySource(value = "classpath:datasource.properties")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.example.spring.cahce.repository"})
public class JpaConfiguration {

    @Autowired
    private Environment environment;

    /**
     * Additional name since, EnableJpaRepositories search for bean with 'entityManagerFactory' name
     * we can override default expected name though.
     * <p>
     * This LocalEntityManagerFactoryBean using META-INF/persistence.xml file
     * We are just override DB connection related properties. Drawback of LocalEntityManagerFactoryBean is
     * we can not pass application managed DataSource to it, we need to use hibernate dataSource.
     * <p>
     * Therefore this LocalEntityManagerFactoryBean, should not be used for production ready applications.
     */
    @Bean(name = {"entityManagerFactory"})
    @Profile("dev")
    public LocalEntityManagerFactoryBean localEntityManagerFactoryBean(final PersistenceUnitPropertySource propertySource,
                                                                       @Value(value = "${PERSISTENCE_UNIT_NAME}") String persistenceUnitName) {
        final LocalEntityManagerFactoryBean localEntityManagerFactoryBean = new LocalEntityManagerFactoryBean();
        localEntityManagerFactoryBean.setPersistenceUnitName(persistenceUnitName);

        localEntityManagerFactoryBean.setJpaPropertyMap(propertySource.getPropertyMap());

        return localEntityManagerFactoryBean;
    }

    /**
     * JpaTransactionManager to manage transactions in application as a implementation of PlatformTransactionManager.
     *
     * @param entityManagerFactoryBean EntityManagerFactory bean based on profile selected.
     */
    @Bean(name = {"transactionManager"})
    public PlatformTransactionManager jpaTransactionManager(AbstractEntityManagerFactoryBean entityManagerFactoryBean) {
        final JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactoryBean.getObject());

        return jpaTransactionManager;
    }

    /**
     * Define vendor adaptor for LocalContainerEntityManagerFactory
     * @return
     */
    @Bean
    @Profile(value = {"int", "prod"})
    public JpaVendorAdapter hibernateJpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.MYSQL);
        jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL57Dialect");
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setShowSql(true);

        return jpaVendorAdapter;
    }

    /**
     * Define container managed DataSource which we will pass to LocalContainerEntityManagerFactory
     *
     * @return
     */
    @Bean
    @Profile(value = {"int", "prod"})
    public DataSource hikariCpDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(this.environment.getProperty("datasource.url"));
        config.setUsername(this.environment.getProperty("DB_USERNAME"));
        config.setPassword(this.environment.getProperty("DB_PASSWORD"));

        return new HikariDataSource(config);
    }

    /**
     * We do not need to use persistence.xml at all. We can programmatically configure everything.
     * best part about {@link LocalContainerEntityManagerFactoryBean} is we can pass container managed DataSource to it.
     *
     * @param dataSource       DataSource to be used
     * @param jpaVendorAdapter VendorAdaptor that abstracts persistence provider specific configuration/properties.
     */
    @Bean(name = {"entityManagerFactory"})
    @Profile(value = {"int", "prod"})
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(final DataSource dataSource,
                                                                                         final JpaVendorAdapter jpaVendorAdapter, final PersistenceUnitPropertySource propertySource) {
        final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);

        // here we are defining completely new persistence unit programmatically.
        entityManagerFactoryBean.setPersistenceUnitName("my-programmatic-pu");

        /*
         if we are not using persistence.xml, and not using JPA way of scanning Entities (which scans all classes and
         jars in classpath, so it takes lot of time for large projects), and we want to use spring entity scanning
         feature, then we need to provide base packages/classes where to look for entities.
        */
        entityManagerFactoryBean.setPackagesToScan("com.example.spring.cahce.model");
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactoryBean.setJpaPropertyMap(propertySource.getPropertyMap());

        return entityManagerFactoryBean;
    }

    /**
     * Hibernate collects statistics at SessionFactory level. So expose this statistics as a bean so that we should able to use it for
     * assertions in test cases
     *
     * @param entityManagerFactory EntityManagerFactory for which we want to get statistics.
     * @return Statistics attached to entityManagerFactory
     */
    @Bean
    public Statistics hibernateStats(final EntityManagerFactory entityManagerFactory) {
        final SessionFactory underlyingSessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        return underlyingSessionFactory.getStatistics();
    }
}
