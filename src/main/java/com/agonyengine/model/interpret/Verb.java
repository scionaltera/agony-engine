package com.agonyengine.model.interpret;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Verb {
    @Id
    private String name;
    private int priority;
    private boolean quoting = false;
    private String bean;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isQuoting() {
        return quoting;
    }

    public void setQuoting(boolean quoting) {
        this.quoting = quoting;
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }
}
