package com.agonyengine.resource.annotation;

import com.agonyengine.resource.model.AccountRegistration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class UniqueUsernameValidatorTest {
    @Mock
    private AccountRegistration registration;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private UserDetailsManager userDetailsManager;

    private UniqueUsernameValidator validator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(registration.getUsername()).thenReturn("Frank");

        validator = new UniqueUsernameValidator(userDetailsManager);
    }

    @Test
    public void testValid() {
        when(userDetailsManager.userExists(eq("Frank"))).thenReturn(false);

        boolean result = validator.isValid(registration, context);

        assertTrue(result);
    }

    @Test
    public void testInvalid() {
        when(userDetailsManager.userExists(eq("Frank"))).thenReturn(true);

        boolean result = validator.isValid(registration, context);

        assertFalse(result);
    }
}
