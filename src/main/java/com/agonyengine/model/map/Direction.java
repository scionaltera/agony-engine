package com.agonyengine.model.map;

public class Direction {
    private String name;
    private String opposite;
    private int x;
    private int y;
    private int z;

    public Direction(String name, String opposite, int x, int y, int z) {
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
