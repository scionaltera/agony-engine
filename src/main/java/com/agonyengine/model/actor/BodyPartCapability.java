package com.agonyengine.model.actor;

import com.agonyengine.model.util.BitfieldLabel;

public final class BodyPartCapability extends BitfieldLabel {
    public static final BodyPartCapability CAN_WALK = new BodyPartCapability(1, "walk");

    private BodyPartCapability(int index, String name) {
        super(index, name);
    }
}
