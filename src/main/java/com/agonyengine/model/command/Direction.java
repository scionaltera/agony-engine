package com.agonyengine.model.command;

public class Direction {
    private String name;
    private String opposite;
    private int x;
    private int y;

    public Direction(String name, String opposite, int x, int y) {
        this.name = name;
        this.opposite = opposite;
        this.x = x;
        this.y = y;
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
}
