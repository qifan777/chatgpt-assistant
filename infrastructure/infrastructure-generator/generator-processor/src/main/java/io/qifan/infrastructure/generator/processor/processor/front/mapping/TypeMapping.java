package io.qifan.infrastructure.generator.processor.processor.front.mapping;


import io.qifan.infrastructure.generator.processor.model.common.Field;

public interface TypeMapping {
    String convert(Field field);
}
