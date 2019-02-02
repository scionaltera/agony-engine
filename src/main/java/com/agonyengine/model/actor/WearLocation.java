package com.agonyengine.model.actor;

import com.agonyengine.model.util.BaseEnumSetConverter;
import com.agonyengine.model.util.PersistentEnum;

public enum WearLocation implements PersistentEnum {
    HEAD(0),
    NECK(1),
    WRIST(2),
    HAND(3),
    FINGER(4),
    FOOT(5),
    BODY_UPPER(6),
    BODY_LOWER(7),
    ARM_UPPER(8),
    ARM_LOWER(9),
    LEG_UPPER(10),
    LEG_LOWER(11);

    private int index;

    WearLocation(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }

    public static class Converter extends BaseEnumSetConverter<WearLocation> {
        public Converter() {
            super(WearLocation.class);
        }
    }
}
