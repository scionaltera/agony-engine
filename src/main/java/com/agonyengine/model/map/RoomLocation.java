package com.agonyengine.model.map;

import javax.persistence.Embeddable;

// TODO rename to Location after the other Location is gone
@Embeddable
public class RoomLocation {
    private Long x;
    private Long y;
    private Long z;

    public RoomLocation() {
        // this method intentionally left blank
    }

    public RoomLocation(Long x, Long y) {
        setX(x);
        setY(y);
        setZ(0L);
    }

    public RoomLocation(Long x, Long y, Long z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public Long getZ() {
        return z;
    }

    public void setZ(Long z) {
        this.z = z;
    }
}
