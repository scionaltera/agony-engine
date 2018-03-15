package com.agonyengine.core.resource;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MainResourceTest {
    private MainResource resource;

    @Before
    public void setUp() {
        resource = new MainResource();
    }

    @Test
    public void testIndex() {
        assertEquals("index", resource.index());
    }
}
