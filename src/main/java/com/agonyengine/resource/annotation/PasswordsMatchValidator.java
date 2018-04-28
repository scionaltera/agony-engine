package com.agonyengine.resource.annotation;

import com.agonyengine.resource.model.AccountRegistration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, AccountRegistration> {
    @Override
    public boolean isValid(AccountRegistration registration, ConstraintValidatorContext context) {
        return registration.getPassword().equals(registration.getPasswordConfirm());
    }
}
