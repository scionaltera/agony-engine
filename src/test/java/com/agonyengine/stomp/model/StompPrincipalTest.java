package com.agonyengine.stomp.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class StompPrincipalTest {
    private StompPrincipal principal;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testName() {
        String name = "Witch";

        principal = new StompPrincipal(name);

        assertEquals(name, principal.getName());
    }
}
