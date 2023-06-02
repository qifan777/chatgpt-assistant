package io.qifan.infrastructure.generator.processor.model.front;

import io.qifan.infrastructure.generator.processor.model.common.Type;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class Api extends FrontModel {
    private Type entityType;

    @Override
    public Set<Type> getImportTypes() {
        return null;
    }

    @Override
    public String getFileName() {
        String[] split = entityType.getTypeName()
                                   .split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
        String joinString = StringUtils.arrayToDelimitedString(split, "-");
        return joinString.toLowerCase() + ".ts";
    }
}
