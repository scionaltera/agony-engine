package com.agonyengine.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FormattingUtilsTest {
    @Test
    public void testAoranConsonant() {
        assertEquals("a sword", FormattingUtils.aoran("sword"));
    }

    @Test
    public void testAoranVowel() {
        assertEquals("an article", FormattingUtils.aoran("article"));
    }

    @Test
    public void testAoranNeither() {
        assertEquals("12345", FormattingUtils.aoran("12345"));
    }

    @Test
    public void testAoranSomething() {
        assertEquals("something awesome", FormattingUtils.aoran("something awesome"));
    }
}
