package com.agonyengine.model.actor;

import com.agonyengine.model.converter.BaseEnumSetConverter;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

public enum BodyPartCapability {
    SPEAK("speak"),
    WALK("walk"),
    HOLD("hold items"),
    SEE("see");

    private String description;

    BodyPartCapability(String description) {
        this.description = description;
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
