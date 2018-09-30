package com.qthegamep.bookmanager3.config;

import com.qthegamep.bookmanager3.testhelper.rule.Rules;

import lombok.val;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import static org.assertj.core.api.Assertions.*;

public class AppInitializerTest {

    @ClassRule
    public static ExternalResource summaryRule = Rules.SUMMARY_RULE;

    @Rule
    public Stopwatch stopwatchRule = Rules.STOPWATCH_RULE;

    private AppInitializer appInitializer;

    @Before
    public void setUp() {
        appInitializer = new AppInitializer();
    }

    @Test
    public void shouldCreateObjectWithNoArgsConstructor() {
        assertThat(appInitializer).isNotNull();
    }

    @Test
    public void shouldImplementsAbstractAnnotationConfigDispatcherServletInitializerInterface() {
        assertThat(appInitializer).isInstanceOf(AbstractAnnotationConfigDispatcherServletInitializer.class);
    }

    @Test
    public void shouldGetRootConfigClassesCorrectly() {
        val rootConfigClasses = appInitializer.getRootConfigClasses();

        assertThat(rootConfigClasses)
                .isNotNull()
                .containsOnly(AppConfig.class);
    }

    @Test
    public void shouldGetServletConfigClassesCorrectly() {
        val servletConfigClasses = appInitializer.getServletConfigClasses();

        assertThat(servletConfigClasses)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void shouldGetServletMappingsCorrectly() {
        val servletMappings = appInitializer.getServletMappings();

        assertThat(servletMappings)
                .isNotNull()
                .isEmpty();
    }
}
