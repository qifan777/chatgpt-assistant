package io.qifan.infrastructure.generator.processor.processor.front.mapping;

import io.qifan.infrastructure.generator.processor.model.common.Field;
import lombok.SneakyThrows;

import java.util.List;

public class StringMapping implements TypeMapping {


    @SneakyThrows
    @Override
    public String convert(Field field) {
        List<String> types = List.of("char", "Character", "String", "LocalDateTime", "Date",
                                     "UUID");
        return types.contains(field.getType().getTypeName()) ? "string" : null;
    }
}