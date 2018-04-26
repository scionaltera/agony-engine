package com.agonyengine.core.resource.model;

import com.agonyengine.core.resource.annotation.PasswordsMatch;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@PasswordsMatch(message = "Password and confirmation must match.")
public class AccountRegistration {
    @NotNull(message = "Username must not be empty.")
    @Size(min = 3, message = "Username must be at least 3 letters long.")
    @Pattern(regexp = "[a-z0-9]{3,}", flags={Pattern.Flag.CASE_INSENSITIVE}, message = "Username may only contain letters and numbers.")
    private String username;

    @NotNull(message = "Password must not be empty.")
    @Size(min = 6, message = "Password must be at least 6 letters long.")
    private String password;

    @NotNull(message = "Password confirmation must not be empty.")
    @Size(min = 6, message = "Password confirmation must be at least 6 letters long.")
    private String passwordConfirm;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
