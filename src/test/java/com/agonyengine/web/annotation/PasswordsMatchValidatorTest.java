package com.agonyengine.web.annotation;

import com.agonyengine.web.model.AccountRegistration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class PasswordsMatchValidatorTest {
    @Mock
    private AccountRegistration registration;

    @Mock
    private ConstraintValidatorContext context;

    private PasswordsMatchValidator validator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        validator = new PasswordsMatchValidator();
    }

    @Test
    public void testPasswordsMatch() {
        when(registration.getPassword()).thenReturn("correcthorsebatterystaple");
        when(registration.getPasswordConfirm()).thenReturn("correcthorsebatterystaple");

        boolean result = validator.isValid(registration, context);

        assertTrue(result);
    }

    @Test
    public void testPasswordsDoNotMatch() {
        when(registration.getPassword()).thenReturn("incorrecthorsebatterystaple");
        when(registration.getPasswordConfirm()).thenReturn("correcthorsebatterystaple");

        boolean result = validator.isValid(registration, context);

        assertFalse(result);
    }
}
