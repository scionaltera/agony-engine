package com.agonyengine.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NameUtilsTest {
    @Test
    public void testAoranConsonant() {
        assertEquals("a sword", NameUtils.aoran("sword"));
    }

    @Test
    public void testAoranVowel() {
        assertEquals("an article", NameUtils.aoran("article"));
    }

    @Test
    public void testAoranNeither() {
        assertEquals("12345", NameUtils.aoran("12345"));
    }

    @Test
    public void testAoranSomething() {
        assertEquals("something awesome", NameUtils.aoran("something awesome"));
    }
}
