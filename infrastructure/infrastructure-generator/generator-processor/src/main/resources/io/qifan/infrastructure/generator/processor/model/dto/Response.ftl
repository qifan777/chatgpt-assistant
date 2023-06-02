<#-- @ftlvariable name="" type="io.qifan.infrastructure.generator.processor.model.dto.Response" -->
package ${type.packagePath};
<#list importTypes as importType>
    import ${importType.getTypePath()};
</#list>

<#--/**-->
<#--* A DTO for the {@link ${getEntityType().typeName}} entity-->
<#--*/-->

@Data
public class ${type.typeName} extends BaseResponse {
<#list fields as field >
    <@includeModel object=field></@includeModel>
</#list>
}