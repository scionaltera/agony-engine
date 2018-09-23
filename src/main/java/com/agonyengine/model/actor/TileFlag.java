package com.agonyengine.model.actor;

import com.agonyengine.model.converter.BaseEnumSetConverter;

public enum TileFlag {
    IMPASSABLE;

    public static class Converter extends BaseEnumSetConverter<TileFlag> {
        public Converter() {
            super(TileFlag.class);
        }
    }
}
