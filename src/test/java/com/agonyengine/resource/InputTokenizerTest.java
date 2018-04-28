package com.agonyengine.resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class InputTokenizerTest {
    private InputTokenizer inputTokenizer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        inputTokenizer = new InputTokenizer();
    }

    @Test
    public void testNull() {
        List<String[]> tokens = inputTokenizer.tokenize(null);

        assertEquals(0, tokens.size());
    }

    @Test
    public void testEmpty() {
        List<String[]> tokens = inputTokenizer.tokenize("");

        assertEquals(0, tokens.size());
    }

    @Test
    public void testSingleWord() {
        List<String[]> tokens = inputTokenizer.tokenize("word");

        assertEquals(1, tokens.size());
        assertEquals(1, tokens.get(0).length);
        assertEquals("WORD", tokens.get(0)[0]);
    }

    @Test
    public void testTwoWords() {
        List<String[]> tokens = inputTokenizer.tokenize("two words");

        assertEquals(1, tokens.size());
        assertEquals(2, tokens.get(0).length);
        assertEquals("TWO", tokens.get(0)[0]);
        assertEquals("WORDS", tokens.get(0)[1]);
    }

    @Test
    public void testEntireStringIsQuoted() {
        List<String[]> tokens = inputTokenizer.tokenize("\"quoted string\"");

        assertEquals(1, tokens.size());
        assertEquals(1, tokens.get(0).length);
        assertEquals("quoted string", tokens.get(0)[0]);
    }

    @Test
    public void testMiddleOfStringIsQuoted() {
        List<String[]> tokens = inputTokenizer.tokenize("the \"middle is\" quoted");

        assertEquals(1, tokens.size());
        assertEquals(3, tokens.get(0).length);
        assertEquals("THE", tokens.get(0)[0]);
        assertEquals("middle is", tokens.get(0)[1]);
        assertEquals("QUOTED", tokens.get(0)[2]);
    }

    @Test
    public void testEndOfStringIsQuoted() {
        List<String[]> tokens = inputTokenizer.tokenize("the end \"is quoted\"");

        assertEquals(1, tokens.size());
        assertEquals(3, tokens.get(0).length);
        assertEquals("THE", tokens.get(0)[0]);
        assertEquals("END", tokens.get(0)[1]);
        assertEquals("is quoted", tokens.get(0)[2]);
    }

    @Test
    public void testEmptyQuotedString() {
        List<String[]> tokens = inputTokenizer.tokenize("empty \"\" quote");

        assertEquals(1, tokens.size());
        assertEquals(2, tokens.get(0).length);
        assertEquals("EMPTY", tokens.get(0)[0]);
        assertEquals("QUOTE", tokens.get(0)[1]);
    }

    @Test
    public void testPeriods() {
        List<String[]> tokens = inputTokenizer.tokenize("this is. two sentences");

        assertEquals(2, tokens.size());
        assertEquals(2, tokens.get(0).length);
        assertEquals(2, tokens.get(1).length);
        assertEquals("THIS", tokens.get(0)[0]);
        assertEquals("IS", tokens.get(0)[1]);
        assertEquals("TWO", tokens.get(1)[0]);
        assertEquals("SENTENCES", tokens.get(1)[1]);
    }

    @Test
    public void testExclamations() {
        List<String[]> tokens = inputTokenizer.tokenize("this is! two sentences");

        assertEquals(2, tokens.size());
        assertEquals(2, tokens.get(0).length);
        assertEquals(2, tokens.get(1).length);
        assertEquals("THIS", tokens.get(0)[0]);
        assertEquals("IS", tokens.get(0)[1]);
        assertEquals("TWO", tokens.get(1)[0]);
        assertEquals("SENTENCES", tokens.get(1)[1]);
    }

    @Test
    public void testQuestions() {
        List<String[]> tokens = inputTokenizer.tokenize("this is? two sentences");

        assertEquals(2, tokens.size());
        assertEquals(2, tokens.get(0).length);
        assertEquals(2, tokens.get(1).length);
        assertEquals("THIS", tokens.get(0)[0]);
        assertEquals("IS", tokens.get(0)[1]);
        assertEquals("TWO", tokens.get(1)[0]);
        assertEquals("SENTENCES", tokens.get(1)[1]);
    }

    @Test
    public void testExtraWhitespace() {
        List<String[]> tokens = inputTokenizer.tokenize("much     whitespace");

        assertEquals(1, tokens.size());
        assertEquals(2, tokens.get(0).length);
        assertEquals("MUCH", tokens.get(0)[0]);
        assertEquals("WHITESPACE", tokens.get(0)[1]);
    }

    @Test
    public void testMultiplePunctuations() {
        List<String[]> tokens = inputTokenizer.tokenize("test...");

        assertEquals(1, tokens.size());
        assertEquals(1, tokens.get(0).length);
        assertEquals("TEST", tokens.get(0)[0]);
    }

    @Test
    public void testEmbeddedQuotes() {
        List<String[]> tokens = inputTokenizer.tokenize("code: \"System.out.println(\"Test\");\" code");

        assertEquals(1, tokens.size());
        assertEquals(5, tokens.get(0).length);
        assertEquals("CODE", tokens.get(0)[0]);
        assertEquals("System.out.println(", tokens.get(0)[1]);
        assertEquals("TEST", tokens.get(0)[2]);
        assertEquals(");", tokens.get(0)[3]);
        assertEquals("CODE", tokens.get(0)[4]);
    }
}
