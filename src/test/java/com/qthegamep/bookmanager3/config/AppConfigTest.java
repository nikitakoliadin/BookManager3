package com.qthegamep.bookmanager3.config;

import com.qthegamep.bookmanager3.testhelper.rule.Rules;

import lombok.val;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;

@WebAppConfiguration
@DirtiesContext
@ContextConfiguration(classes = AppConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class AppConfigTest {

    @ClassRule
    public static ExternalResource summaryRule = Rules.SUMMARY_RULE;

    @Rule
    public Stopwatch stopwatchRule = Rules.STOPWATCH_RULE;

    @Autowired
    private AppConfig appConfig;

    @Test
    public void shouldAutowiredAppConfig() {
        assertThat(appConfig).isNotNull();
    }

    @Test
    public void shouldBeCorrectlySettingInDataSourceBeanDriverClassName() throws SQLException {
        val dataSource = (DriverManagerDataSource) appConfig.dataSource();

        val driverClassName = DriverManager
                .getDriver(dataSource
                        .getConnection()
                        .getMetaData()
                        .getURL())
                .getClass();

        assertThat(driverClassName).isNotNull();
    }

    @Test
    public void shouldBeCorrectlySettingInDataSourceBeanUrl() {
        val dataSource = (DriverManagerDataSource) appConfig.dataSource();

        val url = dataSource.getUrl();

        assertThat(url)
                .isNotNull()
                .isNotEmpty()
                .startsWith("jdbc:");
    }

    @Test
    public void shouldBeCorrectlySettingInDataSourceBeanUsername() {
        val dataSource = (DriverManagerDataSource) appConfig.dataSource();

        val username = dataSource.getUsername();

        assertThat(username)
                .isNotNull()
                .isNotEmpty();
    }

    @Test
    public void shouldBeCorrectlySettingInDataSourceBeanPassword() {
        val dataSource = (DriverManagerDataSource) appConfig.dataSource();

        val password = dataSource.getPassword();

        assertThat(password).isNotNull();
    }

    @Test
    public void shouldBeCorrectlySettingInEntityManagerFactoryBeanDataSource() {
        val entityManagerFactory = appConfig.entityManagerFactory();

        val actualDataSource = entityManagerFactory.getDataSource();

        val expectedDataSource = appConfig.dataSource();

        assertThat(actualDataSource)
                .isNotNull()
                .isEqualTo(expectedDataSource);
    }

    @Test
    public void shouldBeCorrectlySettingInEntityManagerFactoryBeanPersistenceUnitName() {
        val entityManagerFactory = appConfig.entityManagerFactory();

        val persistenceUnitName = entityManagerFactory.getPersistenceUnitName();

        assertThat(persistenceUnitName)
                .isNotNull()
                .isNotEmpty()
                .isNotEqualTo("default");
    }

    @Test
    public void shouldBeCorrectlySettingInEntityManagerFactoryBeanPersistenceProviderClass() {
        val entityManagerFactory = appConfig.entityManagerFactory();

        val persistenceProviderClass = Objects
                .requireNonNull(entityManagerFactory.getPersistenceProvider())
                .getClass();

        assertThat(persistenceProviderClass)
                .isNotNull()
                .isEqualTo(HibernatePersistenceProvider.class);
    }

    @Test
    public void shouldBeCorrectlySettingInEntityManagerFactoryBeanJpaProperties() {
        val entityManagerFactory = appConfig.entityManagerFactory();

        val jpaPropertyMap = entityManagerFactory.getJpaPropertyMap();

        assertThat(jpaPropertyMap)
                .isNotNull()
                .isNotEmpty();
    }

    @Test
    public void shouldBeCorrectlySettingInTransactionManagerBeanEntityManagerFactory() {
        val transactionManager = appConfig.transactionManager();

        val actualEntityManagerFactory = transactionManager.getEntityManagerFactory();

        val expectedEntityManagerFactory = appConfig.entityManagerFactory().getObject();

        assertThat(actualEntityManagerFactory)
                .isNotNull()
                .isEqualTo(expectedEntityManagerFactory);
    }
}
