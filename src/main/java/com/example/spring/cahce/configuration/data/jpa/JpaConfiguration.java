package com.example.spring.cahce.configuration.data.jpa;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author amit
 */
@Configuration
@PropertySource(value = "classpath:datasource.properties")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.example.spring.cahce.repository"})
public class JpaConfiguration {

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

}
