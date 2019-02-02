package com.agonyengine.model.map;

import com.agonyengine.model.util.BaseEnumSetConverter;
import com.agonyengine.model.util.PersistentEnum;

public enum Direction implements PersistentEnum {
    NORTH(0, "north", "south", 0, 1, 0),
    EAST(1, "east", "west", 1, 0, 0),
    SOUTH(2, "south", "north", 0, -1, 0),
    WEST(3, "west", "east", -1, 0, 0);

    private int index;
    private String name;
    private String opposite;
    private int x;
    private int y;
    private int z;

    Direction(int index, String name, String opposite, int x, int y, int z) {
        this.index = index;
        this.name = name;
        this.opposite = opposite;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getName() {
        return name;
    }

    public String getOpposite() {
        return opposite;
    }

    public Direction toOpposite() {
        return Direction.valueOf(getOpposite().toUpperCase());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public int getIndex() {
        return index;
    }

    public static class Converter extends BaseEnumSetConverter<Direction> {
        public Converter() {
            super(Direction.class);
        }
    }
}
