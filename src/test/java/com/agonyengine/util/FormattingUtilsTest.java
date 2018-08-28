package com.agonyengine.util;

import org.junit.Test;
import org.springframework.util.StringUtils;

import static com.agonyengine.util.FormattingUtils.WRAP_LENGTH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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

    @Test
    public void testSoftWrapShortString() {
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < WRAP_LENGTH; i++) {
            if (i % 7 == 0) {
                buf.append(" ");
            } else {
                buf.append("A");
            }
        }

        String out = FormattingUtils.softWrap(buf.toString());

        assertFalse(out.contains("<br/"));
    }

    @Test
    public void testSoftWrapVeryShortString() {
        String out = FormattingUtils.softWrap("A");

        assertFalse(out.contains("<br/"));
    }

    @Test
    public void testSoftWrapNoSpaces() {
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < WRAP_LENGTH + 1; i++) {
            buf.append("A");
        }

        String out = FormattingUtils.softWrap(buf.toString());

        assertFalse(out.contains("<br/"));
    }

    @Test
    public void testSoftWrapSingleWrap() {
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < (WRAP_LENGTH * 2) + 1; i++) {
            if (i % 7 == 0) {
                buf.append(" ");
            } else {
                buf.append("A");
            }
        }

        String out = FormattingUtils.softWrap(buf.toString());

        assertEquals(1, StringUtils.countOccurrencesOf(out, "<br/>"));
    }

    @Test
    public void testSoftWrapExactLength() {
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < WRAP_LENGTH * 2; i++) {
            if (i % WRAP_LENGTH == 0) {
                buf.append(" ");
            } else {
                buf.append("A");
            }
        }

        String out = FormattingUtils.softWrap(buf.toString());

        assertEquals(1, StringUtils.countOccurrencesOf(out, "<br/>"));
    }

    @Test
    public void testSoftWrapMultipleWraps() {
        int wrapCount = 4;
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < WRAP_LENGTH * wrapCount + 1; i++) {
            if (i % 7 == 0) {
                buf.append(" ");
            } else {
                buf.append("A");
            }
        }

        String out = FormattingUtils.softWrap(buf.toString());

        assertEquals(wrapCount, StringUtils.countOccurrencesOf(out, "<br/>"));
    }
}
