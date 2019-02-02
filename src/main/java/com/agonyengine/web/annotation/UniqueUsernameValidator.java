package com.agonyengine.web.annotation;

import com.agonyengine.web.model.AccountRegistration;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, AccountRegistration> {
    private UserDetailsManager userDetailsManager;

    @Inject
    public UniqueUsernameValidator(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    @Override
    public boolean isValid(AccountRegistration registration, ConstraintValidatorContext context) {
        return !(userDetailsManager.userExists(registration.getUsername()));
    }
}
