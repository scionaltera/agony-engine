package com.agonyengine.model.actor;

import com.agonyengine.model.util.BaseEnumSetConverter;
import com.agonyengine.model.util.PersistentEnum;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

public enum BodyPartCapability implements PersistentEnum {
    SPEAK(0, "speak"),
    WALK(1, "walk"),
    HOLD(2, "hold items"),
    SEE(3, "see");

    private int index;
    private String description;

    BodyPartCapability(int index, String description) {
        this.index = index;
        this.description = description;
    }

    @Override
    public int getIndex() {
        return index;
    }

    public String getDescription() {
        return description;
    }

    public static String toLabels(EnumSet<BodyPartCapability> bitfield) {
        return Arrays.stream(values())
            .filter(bitfield::contains)
            .map(BodyPartCapability::getDescription)
            .collect(Collectors.joining(", "));
    }

    public static class Converter extends BaseEnumSetConverter<BodyPartCapability> {
        public Converter() {
            super(BodyPartCapability.class);
        }
    }
}
