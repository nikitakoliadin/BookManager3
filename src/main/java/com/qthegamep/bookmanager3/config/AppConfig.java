package com.qthegamep.bookmanager3.config;

import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.springframework.core.env.Environment;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.annotation.Resource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * This class is application configuration class that is responsible for creating and setting beans.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan("com.qthegamep.bookmanager3")
@PropertySource("classpath:application.properties")
@EnableJpaRepositories("com.qthegamep.bookmanager3.repository")
public class AppConfig {

    private static final String DB_DRIVER_CLASS_NAME = "db.driverClassName";
    private static final String DB_URL = "db.url";
    private static final String DB_USERNAME = "db.username";
    private static final String DB_PASSWORD = "db.password";
    private static final String EMF_PERSISTENCE_UNIT_NAME = "emf.persistenceUnitName";
    private static final String EMF_PACKAGES_TO_SCAN = "emf.packagesToScan";
    private static final String HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";
    private static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String HIBERNATE_FORMAT_SQL = "hibernate.format_sql";
    private static final String HIBERNATE_USE_SQL_COMMENTS = "hibernate.use_sql_comments";
    private static final String HIBERNATE_GENERATE_STATISTICS = "hibernate.generate_statistics";
    private static final String HIBERNATE_JDBC_BATCH_SIZE = "hibernate.jdbc.batch_size";
    private static final String HIBERNATE_JDBC_FETCH_SIZE = "hibernate.jdbc.fetch_size";
    private static final String HIBERNATE_ORDER_INSERTS = "hibernate.order_inserts";
    private static final String HIBERNATE_ORDER_UPDATES = "hibernate.order_updates";
    private static final String HIBERNATE_JDBC_BATCH_VERSIONED_DATA = "hibernate.jdbc.batch_versioned_data";

    @Resource
    private Environment env;

    /**
     * Create and setting database source bean.
     *
     * @return {@link javax.sql.DataSource} bean.
     */
    @Bean
    public DataSource dataSource() {
        val dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(env.getRequiredProperty(DB_DRIVER_CLASS_NAME));
        dataSource.setUrl(env.getRequiredProperty(DB_URL));
        dataSource.setUsername(env.getRequiredProperty(DB_USERNAME));
        dataSource.setPassword(env.getRequiredProperty(DB_PASSWORD));

        return dataSource;
    }

    /**
     * Create and setting local container entity manager factory bean.
     *
     * @return {@link org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean} bean.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        val entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setPersistenceUnitName(env.getRequiredProperty(EMF_PERSISTENCE_UNIT_NAME));
        entityManagerFactory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactory.setPackagesToScan(env.getRequiredProperty(EMF_PACKAGES_TO_SCAN));

        entityManagerFactory.setJpaProperties(getJpaProperties());

        return entityManagerFactory;
    }

    /**
     * Create and setting jpa transaction manager bean.
     *
     * @return {@link org.springframework.orm.jpa.JpaTransactionManager} bean.
     */
    @Bean
    public JpaTransactionManager transactionManager() {
        val transactionManager = new JpaTransactionManager();

        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return transactionManager;
    }

    private Properties getJpaProperties() {
        val jpaProperties = new Properties();

        jpaProperties.put(HIBERNATE_DIALECT, env.getRequiredProperty(HIBERNATE_DIALECT));
        jpaProperties.put(HIBERNATE_HBM2DDL_AUTO, env.getRequiredProperty(HIBERNATE_HBM2DDL_AUTO));
        jpaProperties.put(HIBERNATE_SHOW_SQL, env.getRequiredProperty(HIBERNATE_SHOW_SQL));
        jpaProperties.put(HIBERNATE_FORMAT_SQL, env.getRequiredProperty(HIBERNATE_FORMAT_SQL));
        jpaProperties.put(HIBERNATE_USE_SQL_COMMENTS, env.getRequiredProperty(HIBERNATE_USE_SQL_COMMENTS));
        jpaProperties.put(HIBERNATE_GENERATE_STATISTICS, env.getRequiredProperty(HIBERNATE_GENERATE_STATISTICS));
        jpaProperties.put(HIBERNATE_JDBC_BATCH_SIZE, env.getRequiredProperty(HIBERNATE_JDBC_BATCH_SIZE));
        jpaProperties.put(HIBERNATE_JDBC_FETCH_SIZE, env.getRequiredProperty(HIBERNATE_JDBC_FETCH_SIZE));
        jpaProperties.put(HIBERNATE_ORDER_INSERTS, env.getRequiredProperty(HIBERNATE_ORDER_INSERTS));
        jpaProperties.put(HIBERNATE_ORDER_UPDATES, env.getRequiredProperty(HIBERNATE_ORDER_UPDATES));
        jpaProperties.put(HIBERNATE_JDBC_BATCH_VERSIONED_DATA,
                env.getRequiredProperty(HIBERNATE_JDBC_BATCH_VERSIONED_DATA)
        );

        return jpaProperties;
    }
}
