package io.qifan.infrastructure.generator.processor.model.dto;


import io.qifan.infrastructure.generator.processor.model.common.Field;
import io.qifan.infrastructure.generator.processor.model.common.ModelElement;
import io.qifan.infrastructure.generator.processor.model.common.Type;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class Request extends ModelElement {
    private String sourcePath;
    private Type type;
    private Type entityType;
    private List<Field> fields;

    @Override
    public Set<Type> getImportTypes() {
        HashSet<Type> types = new HashSet<>();
        fields.forEach(field -> {
            types.addAll(field.getImportTypes());
        });
//        types.add(entityType);
        return types;
    }
}
