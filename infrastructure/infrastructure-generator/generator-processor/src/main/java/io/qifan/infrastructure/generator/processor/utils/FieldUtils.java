package io.qifan.infrastructure.generator.processor.utils;


import io.qifan.infrastructure.generator.core.GenField;
import io.qifan.infrastructure.generator.processor.model.common.Field;
import io.qifan.infrastructure.generator.processor.model.common.Type;
import org.springframework.util.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldUtils {

    public static List<Field> getFields(TypeElement typeElement) {
        List<? extends Element> enclosedElements = typeElement.getEnclosedElements();
        List<VariableElement> variableElements = ElementFilter.fieldsIn(enclosedElements);
        return variableElements.stream()
                               .map(variableElement -> {
                                   String typeString = variableElement.asType().toString();
                                   // 包含了注解和类型
                                   String[] annotationsAndType = typeString.split(" ");
                                   String typePath = annotationsAndType[annotationsAndType.length - 1];
                                   Type type = TypeUtils.getType(typePath);

                                   Optional<GenField> genFieldOptional =
                                           Optional.ofNullable(variableElement.getAnnotation(GenField.class));
                                   if (isParameterType(typePath)) {
                                       type = TypeUtils.getParameterType(typePath);
                                   }
                                   return Field.builder()
                                               .type(type)
                                               .description(genFieldOptional.map(GenField::description).orElse(""))
                                               .fieldName(variableElement.getSimpleName()
                                                                         .toString())
                                               .build();
                               }).toList();
    }

    public static List<Field> getFieldsForRequest(TypeElement typeElement,
                                                  String[] ignoreFields,
                                                  String packageName,
                                                  String suffix) {
        return getFields(typeElement, ignoreFields, packageName, suffix, genField -> {
            // 过滤条件，忽略为true则返回的为false，这样filter才可以过滤。
            return !Optional.ofNullable(genField).map(GenField::ignoreRequest).orElse(false);
        });
    }

    public static List<Field> getFieldsForResponse(TypeElement typeElement,
                                                   String[] ignoreFields,
                                                   String packageName,
                                                   String suffix) {
        return getFields(typeElement, ignoreFields, packageName, suffix, genField -> {
            // 过滤条件，忽略为true则返回的为false，这样filter才可以过滤。
            return !Optional.ofNullable(genField).map(GenField::ignoreResponse).orElse(false);
        });
    }

    public static List<Field> getFields(TypeElement typeElement,
                                        String[] ignoreFields,
                                        String packageName,
                                        String suffix, Predicate<GenField> predicate) {
        List<? extends Element> enclosedElements = typeElement.getEnclosedElements();
        List<VariableElement> variableElements = ElementFilter.fieldsIn(enclosedElements);
        return variableElements.stream()
                               // 过滤掉ignore的属性
                               .filter(variableElement -> {
                                   GenField annotation = variableElement.getAnnotation(GenField.class);
                                   return !Arrays.asList(ignoreFields)
                                                 .contains(
                                                         variableElement.getSimpleName().toString()) && predicate.test(
                                           annotation);
                               })

                               .map(variableElement -> {
                                   String typeString = variableElement.asType().toString();
                                   // 包含了注解和类型
                                   String[] annotationsAndType = typeString.split(" ");
                                   String typePath = annotationsAndType[annotationsAndType.length - 1];
                                   Type type = TypeUtils.getType(typePath);
                                   if (isParameterType(typePath)) {
                                       type = TypeUtils.getParameterType(typePath);
                                   }
                                   if (isAssociation(variableElement)) {
                                       if (isParameterType(typePath)) {
                                           Matcher matcher = Pattern.compile("<([a-zA-Z.]*)>")
                                                                    .matcher(typePath);
                                           if (matcher.find()) {
                                               String entityTypePath = matcher.group(1);
                                               String[] split = entityTypePath.split("\\.");
                                               String entityTypeName = split[split.length - 1];

                                               String dtoTypePath = entityTypePath.replace(entityTypeName,
                                                                                           packageName + "." + entityTypeName + suffix);
                                               type = TypeUtils.getParameterType(typePath.replace(entityTypePath,
                                                                                                  dtoTypePath));
                                           }
                                       } else {
                                           String[] split = typePath.split("\\.");
                                           String entityTypeName = split[split.length - 1];
                                           String dtoTypePath = typePath.replace(entityTypeName,
                                                                                 packageName + "." + entityTypeName + suffix);
                                           type = TypeUtils.getType(dtoTypePath);
                                       }
                                   }

                                   Optional<GenField> genFieldOptional =
                                           Optional.ofNullable(variableElement.getAnnotation(GenField.class));
                                   if (genFieldOptional.map(field -> StringUtils.hasText(field.dtoType()))
                                                       .orElse(false)) {
                                       type = TypeUtils.getType(variableElement, genFieldOptional.orElse(null));
                                   }
                                   return Field.builder()
                                               .description(genFieldOptional.map(GenField::description).orElse(""))
                                               .fieldName(variableElement.getSimpleName()
                                                                         .toString())
                                               .type(type)
                                               .build();
                               }).toList();
    }


    public static boolean isAssociation(VariableElement variableElement) {
        return Optional.ofNullable(variableElement.getAnnotation(GenField.class))
                       .map(GenField::association)
                       .orElse(false);
    }

    public static boolean isParameterType(String typePath) {
        return typePath.contains("<");
    }
}
