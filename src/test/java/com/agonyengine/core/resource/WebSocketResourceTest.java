package com.agonyengine.core.resource;

import com.agonyengine.core.model.stomp.GameOutput;
import com.agonyengine.core.model.stomp.UserInput;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class WebSocketResourceTest {
    @Mock
    private UserInput input;

    private WebSocketResource resource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(input.getInput()).thenReturn("Alpha!");

        resource = new WebSocketResource();
    }

    @Test
    public void testOnSubscribe() {
        GameOutput output = resource.onSubscribe();

        assertTrue(output.getOutput().stream().anyMatch(line -> line.equals("Subscribed!")));
    }

    @Test
    public void testOnInput() {
        GameOutput output = resource.onInput(input);

        assertTrue(output.getOutput().stream().anyMatch(line -> line.equals("Echo: Alpha!")));
    }
}
