<#-- @ftlvariable name="" type="io.qifan.infrastructure.generator.processor.model.repository.Repository" -->
package ${type.packagePath};

<#list importTypes as importType>
    import ${importType.getTypePath()};
</#list>
public interface ${type.getTypeName()} extends BaseRepository<${entityType.getTypeName()}> { }
