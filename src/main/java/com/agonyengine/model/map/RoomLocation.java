package com.agonyengine.model.map;

import javax.persistence.Embeddable;

// TODO rename to Location after the other Location is gone
@Embeddable
public class RoomLocation {
    private Integer x;
    private Integer y;
    private Integer z;

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getZ() {
        return z;
    }

    public void setZ(Integer z) {
        this.z = z;
    }
}
