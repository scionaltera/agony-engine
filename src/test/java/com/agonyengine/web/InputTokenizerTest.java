package com.agonyengine.web;

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
        List<List<String>> tokens = inputTokenizer.tokenize(null);

        assertEquals(0, tokens.size());
    }

    @Test
    public void testEmpty() {
        List<List<String>> tokens = inputTokenizer.tokenize("");

        assertEquals(0, tokens.size());
    }

    @Test
    public void testSingleWord() {
        List<List<String>> tokens = inputTokenizer.tokenize("word");

        assertEquals(1, tokens.size());
        assertEquals(1, tokens.get(0).size());
        assertEquals("WORD", tokens.get(0).get(0));
    }

    @Test
    public void testTwoWords() {
        List<List<String>> tokens = inputTokenizer.tokenize("two words");

        assertEquals(1, tokens.size());
        assertEquals(2, tokens.get(0).size());
        assertEquals("TWO", tokens.get(0).get(0));
        assertEquals("WORDS", tokens.get(0).get(1));
    }

    @Test
    public void testEntireStringIsQuoted() {
        List<List<String>> tokens = inputTokenizer.tokenize("\"quoted string\"");

        assertEquals(1, tokens.size());
        assertEquals(1, tokens.get(0).size());
        assertEquals("quoted string", tokens.get(0).get(0));
    }

    @Test
    public void testMiddleOfStringIsQuoted() {
        List<List<String>> tokens = inputTokenizer.tokenize("in the \"middle is\" quoted");

        assertEquals(1, tokens.size());
        assertEquals(3, tokens.get(0).size());
        assertEquals("IN", tokens.get(0).get(0));
        assertEquals("middle is", tokens.get(0).get(1));
        assertEquals("QUOTED", tokens.get(0).get(2));
    }

    @Test
    public void testEndOfStringIsQuoted() {
        List<List<String>> tokens = inputTokenizer.tokenize("the end \"is quoted\"");

        assertEquals(1, tokens.size());
        assertEquals(2, tokens.get(0).size());
        assertEquals("END", tokens.get(0).get(0));
        assertEquals("is quoted", tokens.get(0).get(1));
    }

    @Test
    public void testEmptyQuotedString() {
        List<List<String>> tokens = inputTokenizer.tokenize("empty \"\" quote");

        assertEquals(1, tokens.size());
        assertEquals(2, tokens.get(0).size());
        assertEquals("EMPTY", tokens.get(0).get(0));
        assertEquals("QUOTE", tokens.get(0).get(1));
    }

    @Test
    public void testThen() {
        List<List<String>> tokens = inputTokenizer.tokenize("do this then do that");

        assertEquals(2, tokens.size());
        assertEquals(2, tokens.get(0).size());
        assertEquals(2, tokens.get(1).size());
        assertEquals("DO", tokens.get(0).get(0));
        assertEquals("THIS", tokens.get(0).get(1));
        assertEquals("DO", tokens.get(1).get(0));
        assertEquals("THAT", tokens.get(1).get(1));
    }

    @Test
    public void testThenAtEnd() {
        List<List<String>> tokens = inputTokenizer.tokenize("do this then");

        assertEquals(1, tokens.size());
        assertEquals(2, tokens.get(0).size());
        assertEquals("DO", tokens.get(0).get(0));
        assertEquals("THIS", tokens.get(0).get(1));
    }

    @Test
    public void testThenAtStart() {
        List<List<String>> tokens = inputTokenizer.tokenize("then do that");

        assertEquals(1, tokens.size());
        assertEquals(2, tokens.get(0).size());
        assertEquals("DO", tokens.get(0).get(0));
        assertEquals("THAT", tokens.get(0).get(1));
    }

    @Test
    public void testPeriods() {
        List<List<String>> tokens = inputTokenizer.tokenize("this is. two sentences");

        assertEquals(2, tokens.size());
        assertEquals(2, tokens.get(0).size());
        assertEquals(2, tokens.get(1).size());
        assertEquals("THIS", tokens.get(0).get(0));
        assertEquals("IS", tokens.get(0).get(1));
        assertEquals("TWO", tokens.get(1).get(0));
        assertEquals("SENTENCES", tokens.get(1).get(1));
    }

    @Test
    public void testExclamations() {
        List<List<String>> tokens = inputTokenizer.tokenize("this is! two sentences");

        assertEquals(2, tokens.size());
        assertEquals(2, tokens.get(0).size());
        assertEquals(2, tokens.get(1).size());
        assertEquals("THIS", tokens.get(0).get(0));
        assertEquals("IS", tokens.get(0).get(1));
        assertEquals("TWO", tokens.get(1).get(0));
        assertEquals("SENTENCES", tokens.get(1).get(1));
    }

    @Test
    public void testQuestions() {
        List<List<String>> tokens = inputTokenizer.tokenize("this is? two sentences");

        assertEquals(2, tokens.size());
        assertEquals(2, tokens.get(0).size());
        assertEquals(2, tokens.get(1).size());
        assertEquals("THIS", tokens.get(0).get(0));
        assertEquals("IS", tokens.get(0).get(1));
        assertEquals("TWO", tokens.get(1).get(0));
        assertEquals("SENTENCES", tokens.get(1).get(1));
    }

    @Test
    public void testExtraWhitespace() {
        List<List<String>> tokens = inputTokenizer.tokenize("much     whitespace");

        assertEquals(1, tokens.size());
        assertEquals(2, tokens.get(0).size());
        assertEquals("MUCH", tokens.get(0).get(0));
        assertEquals("WHITESPACE", tokens.get(0).get(1));
    }

    @Test
    public void testMultiplePunctuations() {
        List<List<String>> tokens = inputTokenizer.tokenize("test...");

        assertEquals(1, tokens.size());
        assertEquals(1, tokens.get(0).size());
        assertEquals("TEST", tokens.get(0).get(0));
    }

    @Test
    public void testPeriodsAtStart() {
        List<List<String>> tokens = inputTokenizer.tokenize("...test");

        assertEquals(1, tokens.size());
        assertEquals(1, tokens.get(0).size());
        assertEquals("TEST", tokens.get(0).get(0));
    }

    @Test
    public void testEmbeddedQuotes() {
        List<List<String>> tokens = inputTokenizer.tokenize("code: \"System.out.println(\"Test\");\" code");

        assertEquals(1, tokens.size());
        assertEquals(5, tokens.get(0).size());
        assertEquals("CODE", tokens.get(0).get(0));
        assertEquals("System.out.println(", tokens.get(0).get(1));
        assertEquals("TEST", tokens.get(0).get(2));
        assertEquals(");", tokens.get(0).get(3));
        assertEquals("CODE", tokens.get(0).get(4));
    }
}
