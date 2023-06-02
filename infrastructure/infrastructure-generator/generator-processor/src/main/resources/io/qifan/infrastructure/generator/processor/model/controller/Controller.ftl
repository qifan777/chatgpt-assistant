<#-- @ftlvariable name="" type="io.qifan.infrastructure.generator.processor.model.controller.Controller" -->
package ${type.packagePath};

<#list importTypes as importType>
    import ${importType.getTypePath()};
</#list>
<#assign uncapitalizeTypeName = entityType.getUncapitalizeTypeName()>

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${uncapitalizeTypeName}")
public class ${entityType.typeName}Controller {
private final ${entityType.typeName}Service ${uncapitalizeTypeName}Service;

@GetMapping("{id}")
public ${entityType.typeName}CommonResponse findById(@PathVariable String id) {
return ${uncapitalizeTypeName}Service.findById(id);
}

@PostMapping("create")
public String create${entityType.typeName}(@RequestBody ${entityType.typeName}CreateRequest createRequest) {
return ${uncapitalizeTypeName}Service.create${entityType.typeName}(createRequest);
}

@PostMapping("{id}/update")
public Boolean update${entityType.typeName}(@RequestBody ${entityType.typeName}UpdateRequest updateRequest, @PathVariable String id) {
${uncapitalizeTypeName}Service.update${entityType.typeName}(updateRequest, id);
return true;
}

@PostMapping("{id}/valid")
public Boolean valid${entityType.typeName}(@PathVariable String id) {
${uncapitalizeTypeName}Service.valid${entityType.typeName}(id);
return true;
}

@PostMapping("{id}/invalid")
public Boolean invalid${entityType.typeName}(@PathVariable String id) {
${uncapitalizeTypeName}Service.invalid${entityType.typeName}(id);
return true;
}

@PostMapping("query")
public Page<${entityType.typeName+'CommonResponse'}> query${entityType.typeName}(
@RequestBody QueryRequest<${entityType.typeName+'QueryRequest'}> queryRequest) {
return ${uncapitalizeTypeName}Service.query${entityType.typeName}(queryRequest);
}

@PostMapping("delete")
public Boolean delete${entityType.typeName}(@RequestBody List<${'String'}> ids) {
return ${uncapitalizeTypeName}Service.delete${entityType.typeName}(ids);
}
}