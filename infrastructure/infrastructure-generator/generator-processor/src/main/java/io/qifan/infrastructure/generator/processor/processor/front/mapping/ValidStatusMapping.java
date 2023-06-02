package io.qifan.infrastructure.generator.processor.processor.front.mapping;


import io.qifan.infrastructure.generator.processor.model.common.Field;

public class ValidStatusMapping implements TypeMapping {
    @Override
    public String convert(Field field) {
        if (!field.getType().getTypeName().equals("ValidStatus")) return null;
        return "'VALID' | 'INVALID'";
    }
}
