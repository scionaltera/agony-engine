package com.agonyengine.model.exit;

import com.agonyengine.model.util.Location;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class ExitTest {
    private Exit exit;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        exit = new Exit();
    }

    @Test
    public void testId() {
        UUID id = UUID.randomUUID();

        exit.setId(id);

        assertEquals(id, exit.getId());
    }

    @Test
    public void testLocation() {
        Location location = new Location();

        exit.setLocation(location);

        assertEquals(location, exit.getLocation());
    }

    @Test
    public void testDestination() {
        Location location = new Location();

        exit.setDestination(location);

        assertEquals(location, exit.getDestination());
    }

    @Test
    public void testDirection() {
        String direction = "north";

        exit.setDirection(direction);

        assertEquals(direction, exit.getDirection());
    }
}
