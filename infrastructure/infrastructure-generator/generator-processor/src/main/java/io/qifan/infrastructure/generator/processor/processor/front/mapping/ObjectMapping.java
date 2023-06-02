package io.qifan.infrastructure.generator.processor.processor.front.mapping;


import io.qifan.infrastructure.generator.processor.model.common.Field;

public class ObjectMapping implements TypeMapping {
    @Override
    public String convert(Field field) {
        return field.getType().getTypeName();
    }
}
