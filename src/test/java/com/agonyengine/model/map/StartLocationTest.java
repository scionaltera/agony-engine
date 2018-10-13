package com.agonyengine.model.map;

import com.agonyengine.model.util.Location;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.Assert.assertEquals;


public class StartLocationTest {
    private StartLocation startLocation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        startLocation = new StartLocation();
    }

    @Test
    public void testId() {
        UUID id = UUID.randomUUID();

        startLocation.setId(id);

        assertEquals(id, startLocation.getId());
    }

    @Test
    public void testLocation() {
        Location location = new Location();

        startLocation.setLocation(location);

        assertEquals(location, startLocation.getLocation());
    }
}
