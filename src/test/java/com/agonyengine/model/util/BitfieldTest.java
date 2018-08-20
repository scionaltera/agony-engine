package com.agonyengine.model.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BitfieldTest {
    private Bitfield bitfield;

    @Before
    public void setUp() {
        bitfield = new Bitfield();
    }

    @Test
    public void testInitialValue() {
        for (int i = 0; i < Long.SIZE; i++) {
            assertFalse(bitfield.isSet(i));
        }
    }

    @Test
    public void testSubset() {
        Bitfield superset = new Bitfield();

        superset.set(0);
        superset.set(1);
        superset.set(2);
        superset.set(3);

        bitfield.set(0);
        bitfield.set(2);
        bitfield.set(3);

        assertTrue(bitfield.isSubset(superset));

        superset.clear(2);

        assertFalse(bitfield.isSubset(superset));
    }

    @Test
    public void testSetEachBit() {
        for (int i = 0; i < Long.SIZE; i++) {
            bitfield.set(i);

            for (int j = 0; j < Long.SIZE; j++) {
                assertEquals(j <= i, bitfield.isSet(j));
            }
        }
    }

    @Test
    public void testClearEachBit() {
        for (int i = 0; i < Long.SIZE; i++) {
            bitfield.set(i);
            assertTrue(bitfield.isSet(i));
        }

        for (int i = 0; i < Long.SIZE; i++) {
            bitfield.clear(i);
            assertFalse(bitfield.isSet(i));
        }
    }

    @Test
    public void testToggleEachBit() {
        for (int i = 0; i < Long.SIZE; i++) {
            assertFalse(bitfield.isSet(i));
            bitfield.toggle(i);
            assertTrue(bitfield.isSet(i));
            bitfield.toggle(i);
            assertFalse(bitfield.isSet(i));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLowerBound() {
        bitfield.isSet(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpperBound() {
        bitfield.isSet(Long.SIZE);
    }
}
