package com.agonyengine.model.actor;

import com.agonyengine.model.util.Bitfield;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class ItemInfoTest {
    private ItemInfo itemInfo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        itemInfo = new ItemInfo();
    }

    @Test
    public void testId() {
        UUID id = UUID.randomUUID();

        itemInfo.setId(id);

        assertEquals(id, itemInfo.getId());
    }

    @Test
    public void testWearLocations() {
        Bitfield bitfield = new Bitfield();

        itemInfo.setWearLocations(bitfield);

        assertEquals(bitfield, itemInfo.getWearLocations());
    }
}
