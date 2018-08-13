package com.agonyengine.resource.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PlayerActorRegistration {
    @NotNull(message = "Given name must not be empty.")
    @Size(min = 3, message = "Given name must be at least 3 letters long.")
    private String givenName;

    @NotNull(message = "Pronouns must be selected.")
    private String pronoun;

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getPronoun() {
        return pronoun;
    }

    public void setPronoun(String pronoun) {
        this.pronoun = pronoun;
    }
}
