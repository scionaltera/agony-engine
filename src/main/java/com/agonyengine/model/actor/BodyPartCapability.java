package com.agonyengine.model.actor;

import com.agonyengine.model.util.Bitfield;

import java.util.Arrays;
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

    public static String toLabels(Bitfield bitfield) {
        return Arrays.stream(values())
            .filter(v -> bitfield.isSet(v.ordinal()))
            .map(BodyPartCapability::getDescription)
            .collect(Collectors.joining(", "));
    }
}
