package com.agonyengine.model.util;

public enum TestPersistentEnum implements PersistentEnum {
    ABLE(0),
    BAKER(1),
    CHARLIE(2),
    DOG(3),
    EASY(4);

    private int index;

    TestPersistentEnum(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }
}
