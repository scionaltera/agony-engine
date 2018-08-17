package com.agonyengine.model.actor;

import com.agonyengine.model.util.BitfieldLabel;

public final class BodyPartCapability extends BitfieldLabel {
    public static final BodyPartCapability WALK = new BodyPartCapability(0, "walk");
    public static final BodyPartCapability SPEAK = new BodyPartCapability(1, "speak");
    public static final BodyPartCapability HOLD = new BodyPartCapability(2, "hold items");

    private BodyPartCapability(int index, String name) {
        super(index, name);
    }
}
