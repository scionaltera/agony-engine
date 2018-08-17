package com.agonyengine.resource.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class PlayerActorRegistrationTest {
    private PlayerActorRegistration playerActorRegistration;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        playerActorRegistration = new PlayerActorRegistration();
    }

    @Test
    public void testGivenName() {
        playerActorRegistration.setGivenName("name");

        assertEquals("name", playerActorRegistration.getGivenName());
    }

    @Test
    public void testPronoun() {
        playerActorRegistration.setPronoun("pronoun");

        assertEquals("pronoun", playerActorRegistration.getPronoun());
    }
}
