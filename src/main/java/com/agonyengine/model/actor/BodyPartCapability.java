package com.agonyengine.model.actor;

import java.util.HashMap;
import java.util.Map;

public final class BodyPartCapability {
    private static final Map<Integer, BodyPartCapability> capabilities = new HashMap<>();

    public static final BodyPartCapability CAN_SEE = new BodyPartCapability(1, "see");
    public static final BodyPartCapability CAN_HEAR = new BodyPartCapability(2, "hear");
    public static final BodyPartCapability CAN_EAT = new BodyPartCapability(3, "eat");
    public static final BodyPartCapability CAN_SPEAK = new BodyPartCapability(4, "speak");
    public static final BodyPartCapability CAN_WALK = new BodyPartCapability(5, "walk");
    public static final BodyPartCapability CAN_HOLD = new BodyPartCapability(6, "hold");
    public static final BodyPartCapability CAN_FLY = new BodyPartCapability(7, "fly");

    private int index;
    private String name;

    public static BodyPartCapability forIndex(int index) {
        return capabilities.get(index);
    }

    private BodyPartCapability(int index, String name) {
        this.index = index;
        this.name = name;

        capabilities.put(index, this);
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }
}
