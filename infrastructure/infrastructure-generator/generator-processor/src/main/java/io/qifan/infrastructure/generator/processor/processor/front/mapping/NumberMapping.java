package io.qifan.infrastructure.generator.processor.processor.front.mapping;

import io.qifan.infrastructure.generator.processor.model.common.Field;
import lombok.SneakyThrows;

import java.util.List;

public class NumberMapping implements TypeMapping {
    @SneakyThrows
    @Override
    public String convert(Field field) {
        List<String> types = List.of("byte", "Byte", "short", "Short", "int", "Integer", "long",
                                     "Long",
                                     "float", "Float", "double", "Double",
                                     "BigDecimal", "BigInteger");
        return types.contains(field.getType().getTypeName()) ? "number" : null;
    }
}