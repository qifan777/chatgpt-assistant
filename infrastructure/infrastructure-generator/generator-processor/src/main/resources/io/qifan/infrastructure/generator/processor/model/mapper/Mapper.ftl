<#-- @ftlvariable name="" type="io.qifan.infrastructure.generator.processor.model.mapper.Mapper" -->
package ${type.packagePath};

<#list importTypes as importType>
    import ${importType.getTypePath()};
</#list>

@Mapper(
uses = {CustomMapper.class},
nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
componentModel = MappingConstants.ComponentModel.SPRING
)
public interface ${getType().getTypeName()} {

${entityType.getTypeName()} createRequest2Entity(${entityType.getTypeName()}CreateRequest request);

${entityType.getTypeName()} queryRequest2Entity(${entityType.getTypeName()}QueryRequest request);

${entityType.getTypeName()} updateEntityFromUpdateRequest(${entityType.getTypeName()}UpdateRequest request, @MappingTarget ${entityType.getTypeName()} entity);

${entityType.getTypeName()}CommonResponse entity2Response(${entityType.getTypeName()} entity);
}