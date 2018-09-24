package com.agonyengine.model.actor;

import com.agonyengine.model.converter.BaseEnumSetConverter;
import com.agonyengine.model.converter.PersistentEnum;

public enum TileFlag implements PersistentEnum {
    IMPASSABLE(0),
    WILDERNESS(1);

    private int index;

    TileFlag(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }

    public static class Converter extends BaseEnumSetConverter<TileFlag> {
        public Converter() {
            super(TileFlag.class);
        }
    }
}
