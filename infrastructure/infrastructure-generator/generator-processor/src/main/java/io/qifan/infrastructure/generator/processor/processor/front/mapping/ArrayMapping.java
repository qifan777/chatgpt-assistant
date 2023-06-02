package io.qifan.infrastructure.generator.processor.processor.front.mapping;

import io.qifan.infrastructure.generator.processor.model.common.Field;
import io.qifan.infrastructure.generator.processor.model.common.ParameterType;
import lombok.SneakyThrows;

import java.util.List;

public class ArrayMapping implements TypeMapping {


    @SneakyThrows
    @Override
    public String convert(Field field) {
        List<String> types = List.of(
                "Set",
                "List",
                "Map",
                "SortedSet",
                "SortedMap",
                "HashSet",
                "TreeSet",
                "ArrayList",
                "LinkedList",
                "Vector",
                "Collections",
                "Arrays",
                "AbstractCollection");
        boolean anyMatch = types.stream().anyMatch(type -> field.getType().getTypeName().contains(type));
        if (!anyMatch) return null;
        if (field.getType() instanceof ParameterType type) {
            ParameterType parameterType = type;
            return parameterType.getParameterType().getCurrentTypeName() + "[]";
        }
        return "any[]";
    }
}