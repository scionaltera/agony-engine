package com.agonyengine.model.util;

import org.junit.Test;

import java.util.EnumSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BaseEnumSetConverterTest {
    private Converter converter = new Converter();

    @Test
    public void testPersist() {
        EnumSet<TestPersistentEnum> testEnumSet = EnumSet.of(
            TestPersistentEnum.ABLE,
            TestPersistentEnum.EASY);

        long result = converter.convertToDatabaseColumn(testEnumSet);

        assertEquals(0b10001, result);
    }

    @Test
    public void testRestore() {
        EnumSet<TestPersistentEnum> result = converter.convertToEntityAttribute((long)0b10001);

        assertTrue(result.contains(TestPersistentEnum.ABLE));
        assertFalse(result.contains(TestPersistentEnum.BAKER));
        assertFalse(result.contains(TestPersistentEnum.CHARLIE));
        assertFalse(result.contains(TestPersistentEnum.DOG));
        assertTrue(result.contains(TestPersistentEnum.EASY));

    }

    public static class Converter extends BaseEnumSetConverter<TestPersistentEnum> {
        Converter() {
            super(TestPersistentEnum.class);
        }
    }
}
