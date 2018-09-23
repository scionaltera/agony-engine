package com.agonyengine.model.actor;

import com.agonyengine.model.converter.BaseEnumSetConverter;

public enum WearLocation {
    HEAD,
    NECK,
    WRIST,
    HAND,
    FINGER,
    FOOT,
    BODY_UPPER,
    BODY_LOWER,
    ARM_UPPER,
    ARM_LOWER,
    LEG_UPPER,
    LEG_LOWER;

    public static class Converter extends BaseEnumSetConverter<WearLocation> {
        public Converter() {
            super(WearLocation.class);
        }
    }
}
