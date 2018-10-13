package com.qthegamep.bookmanager3.exception;

import com.qthegamep.bookmanager3.testhelper.rule.Rules;

import lombok.val;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;

import static org.assertj.core.api.Assertions.*;

public class EntityAlreadyExistsExceptionTest {

    @ClassRule
    public static ExternalResource summaryRule = Rules.SUMMARY_RULE;

    @Rule
    public Stopwatch stopwatchRule = Rules.STOPWATCH_RULE;

    @Test
    public void shouldThrowEntityAlreadyExistsExceptionCorrectly() {
        val exceptionMessage = "test exception message";

        assertThatExceptionOfType(EntityAlreadyExistsException.class)
                .isThrownBy(
                        () -> {
                            throw new EntityAlreadyExistsException(exceptionMessage);
                        })
                .withMessage(exceptionMessage);
    }
}
