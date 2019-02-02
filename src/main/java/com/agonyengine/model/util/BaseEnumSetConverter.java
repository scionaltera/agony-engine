package com.agonyengine.model.util;

import javax.persistence.AttributeConverter;
import java.util.EnumSet;

public abstract class BaseEnumSetConverter<E extends Enum<E> & PersistentEnum> implements AttributeConverter<EnumSet<E>, Long> {
    private final Class<E> klass;

    public BaseEnumSetConverter(Class<E> klass) {
        this.klass = klass;
    }

    @Override
    public Long convertToDatabaseColumn(EnumSet<E> attribute) {
        long total = 0;

        for (E constant : attribute) {
            total |= 1L << constant.getIndex();
        }

        return total;
    }

    @Override
    public EnumSet<E> convertToEntityAttribute(Long dbData) {
        EnumSet<E> results = EnumSet.noneOf(klass);

        for (E constant : klass.getEnumConstants()) {
            if ((dbData & 1L << constant.getIndex()) != 0) {
                results.add(constant);
            }
        }

        return results;
    }
}
