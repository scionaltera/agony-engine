package com.agonyengine.model.converter;

import org.apache.commons.lang3.EnumUtils;

import javax.persistence.AttributeConverter;
import java.util.EnumSet;

public abstract class BaseEnumSetConverter<E extends Enum<E>> implements AttributeConverter<EnumSet<E>, Long> {
    private final Class<E> klass;

    public BaseEnumSetConverter(Class<E> klass) {
        this.klass = klass;
    }

    @Override
    public Long convertToDatabaseColumn(EnumSet<E> attribute) {
        return EnumUtils.generateBitVector(klass, attribute);
    }

    @Override
    public EnumSet<E> convertToEntityAttribute(Long dbData) {
        return EnumUtils.processBitVector(klass, dbData);
    }
}
