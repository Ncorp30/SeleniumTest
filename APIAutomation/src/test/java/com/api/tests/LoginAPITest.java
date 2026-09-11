package com.api.tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginAPITest {

    @Test
    void loginTestPlaceholder() {
        assertNotNull(System.getProperty("user.name"));
        assertFalse(System.getProperty("user.name").isBlank());
    }
}
