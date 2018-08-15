package com.agonyengine.model.actor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Pronoun {
    @Id
    private String subject;
    private String object;
    private String possessive;
    private String possessivePronoun;
    private String reflexive;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getPossessive() {
        return possessive;
    }

    public void setPossessive(String possessive) {
        this.possessive = possessive;
    }

    public String getPossessivePronoun() {
        return possessivePronoun;
    }

    public void setPossessivePronoun(String possessivePronoun) {
        this.possessivePronoun = possessivePronoun;
    }

    public String getReflexive() {
        return reflexive;
    }

    public void setReflexive(String reflexive) {
        this.reflexive = reflexive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pronoun)) return false;
        Pronoun pronoun = (Pronoun) o;
        return Objects.equals(getSubject(), pronoun.getSubject());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getSubject());
    }
}
