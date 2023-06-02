<#-- @ftlvariable name="" type="io.qifan.infrastructure.generator.processor.model.front.Typing" -->
export interface ${getEntityType().typeName} {
<#list typingFields as field>
    ${field.fieldName()}: ${field.typeName()}
</#list>
}
