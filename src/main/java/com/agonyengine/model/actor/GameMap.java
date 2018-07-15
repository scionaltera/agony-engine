package com.agonyengine.model.actor;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class GameMap {
    @Id
    @GeneratedValue
    @Type(type = "pg-uuid")
    private UUID id;
    private int width;
    private byte[] tiles;

    public GameMap() {}

    public GameMap(int width, byte[] tiles) {
        this.width = width;
        this.tiles = tiles;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public byte[] getTiles() {
        return tiles;
    }

    public void setTiles(byte[] tiles) {
        this.tiles = tiles;
    }

    /*
     * Maps are rectangles of {width} tiles on the X axis, where (0, 0) is the bottom left corner. So
     * given the following map:
     *
     * 0x7 0x8 0x9 (2)
     * 0x4 0x5 0x6 (1)
     * 0x1 0x2 0x3 (0)
     * (0) (1) (2)
     *
     * setTile(2, 1, 0x0)
     *
     * ...changes the map to:
     *
     * 0x7 0x8 0x9
     * 0x4 0x5 0x0
     * 0x1 0x2 0x3
     *
     * The following methods are meant to make it easy to deal with the map in terms of coordinates rather than
     * having to deal directly with the byte array.
     */

    public byte getTile(int x, int y) {
        return tiles[computeIndex(x, y)];
    }

    public boolean hasTile(int x, int y) {
        int index = computeIndex(x, y);

        return index >= 0 && index < tiles.length;
    }

    public void setTile(int x, int y, byte value) {
        tiles[computeIndex(x, y)] = value;
    }

    private int computeIndex(int x, int y) {
        if (x < 0 || x >= width) {
            return -1;
        }

        return y * width + x;
    }
}
