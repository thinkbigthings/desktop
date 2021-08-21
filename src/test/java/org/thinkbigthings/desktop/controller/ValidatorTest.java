package org.thinkbigthings.desktop.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.Issue;
import org.junitpioneer.jupiter.IssueProcessor;


import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    Validator validator = new Validator();

    @Issue("REQ-123")
    @DisplayName("Validator identifies strings which are parsable to long values")
    @Test
    void isLong() {

        ServiceLoader<IssueProcessor> issueLoader = ServiceLoader.load(IssueProcessor.class);
        assertTrue(issueLoader.findFirst().isPresent());

        assertTrue(validator.isLong("0"));
    }

    @Test
    @DisplayName("Validator identifies strings which are not parsable to long values")
    void isNotLong() {
        assertFalse(validator.isLong("X"));
    }
}