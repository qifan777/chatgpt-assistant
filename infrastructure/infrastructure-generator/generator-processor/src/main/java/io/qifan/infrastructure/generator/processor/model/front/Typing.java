package io.qifan.infrastructure.generator.processor.model.front;

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
public class Typing extends FrontModel {
    private Type entityType;
    private List<TypingField> typingFields;

    @Override
    public Set<Type> getImportTypes() {
        return new HashSet<>();
    }

    @Override
    public String getFileName() {
        return "index.d.ts";
    }
}
