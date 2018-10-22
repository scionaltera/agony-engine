package com.agonyengine.model.actor;

import com.agonyengine.model.converter.BaseEnumSetConverter;
import com.agonyengine.model.converter.PersistentEnum;

public enum TilesetFlag implements PersistentEnum {
    START_MAP(0);

    private int index;

    TilesetFlag(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }

    public static class Converter extends BaseEnumSetConverter<TilesetFlag> {
        public Converter() {
            super(TilesetFlag.class);
        }
    }
}
