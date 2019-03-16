package com.agonyengine.model.map;

import java.util.Objects;

public class Biome {
    private String name = "Undefined";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Biome)) return false;
        Biome biome = (Biome) o;
        return Objects.equals(getName(), biome.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
