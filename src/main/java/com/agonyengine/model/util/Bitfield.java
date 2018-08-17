package com.agonyengine.model.util;

import javax.persistence.Embeddable;

/*
 * Inspired by bittest.h available at the URL below:
 *
 * https://www.jjj.de/bitwizardry/bitwizardrypage.html
 */
@Embeddable
public class Bitfield {
    private long bits = 0L;

    public boolean isSet(int index) {
        if (index < 0 || index >= Long.SIZE) {
            throw new IllegalArgumentException("Index must be between 0 and " + Long.SIZE);
        }

        return 0 != (bits & (1L << index));
    }

    public void set(int index) {
        bits |= (1L << index);
    }

    public void clear(int index) {
        bits &= ~(1L << index);
    }

    public void toggle(int index) {
        bits ^= (1L << index);
    }
}