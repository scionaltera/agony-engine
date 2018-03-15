package com.agonyengine.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class AgonyEngineTest {
    private AgonyEngine engine;

    @Before
    public void setUp() {
        engine = new AgonyEngine();
    }

    @Test
    public void test() {
        assertNotNull(engine);
    }
}
