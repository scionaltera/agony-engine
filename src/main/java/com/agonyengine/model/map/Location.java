package com.agonyengine.model.map;

import javax.persistence.Embeddable;

@Embeddable
public class Location {
    private Long x;
    private Long y;
    private Long z;

    public Location() {
        // this method intentionally left blank
    }

    public Location(Long x, Long y) {
        setX(x);
        setY(y);
        setZ(0L);
    }

    public Location(Long x, Long y, Long z) {
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

    public void set(Location original) {
        this.x = original.getX();
        this.y = original.getY();
        this.z = original.getZ();
    }

    @Override
    public String toString() {
        return "(" + getX() + ", " + getY() + ", " + getZ() + ")";
    }
}
