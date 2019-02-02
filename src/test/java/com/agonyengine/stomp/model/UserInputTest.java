package com.agonyengine.stomp.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserInputTest {
    private UserInput input;

    @Before
    public void setUp() {
        input = new UserInput();
    }

    @Test
    public void test() {
        input.setInput("Alpha");

        assertEquals("Alpha", input.getInput());

        input.setInput("Beta");

        assertEquals("Beta", input.getInput());
    }
}
