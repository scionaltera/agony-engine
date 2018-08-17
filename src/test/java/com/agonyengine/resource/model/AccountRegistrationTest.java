package com.agonyengine.resource.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class AccountRegistrationTest {
    private AccountRegistration accountRegistration;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        accountRegistration = new AccountRegistration();
    }

    @Test
    public void testUsername() {
        accountRegistration.setUsername("username");

        assertEquals("username", accountRegistration.getUsername());
    }

    @Test
    public void testPassword() {
        accountRegistration.setPassword("password");

        assertEquals("password", accountRegistration.getPassword());
    }

    @Test
    public void testPasswordConfirm() {
        accountRegistration.setPasswordConfirm("password");

        assertEquals("password", accountRegistration.getPasswordConfirm());
    }
}
