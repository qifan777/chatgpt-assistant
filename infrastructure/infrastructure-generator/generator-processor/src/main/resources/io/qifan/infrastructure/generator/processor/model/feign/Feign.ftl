<#-- @ftlvariable name="" type="io.qifan.infrastructure.generator.processor.model.feign.Feign" -->

package ${type.packagePath};

<#list importTypes as importType>
    import ${importType.getTypePath()};
</#list>
<#assign uncapitalizeTypeName = entityType.getUncapitalizeTypeName()>

@FeignClient(value = "${moduleName}", contextId = "${entityType.typeName}", url = "http://localhost:${r'${DAPR_HTTP_PORT}'}/${uncapitalizeTypeName}", configuration = FeignInterceptor.class)
public interface ${entityType.typeName}FeignClient {
@GetMapping("{id}")
R<${'${entityType.typeName}CommonResponse'}> findById(@PathVariable(name = "id") String id);

@PostMapping("create")
R<${'String'}> create${entityType.typeName}(@RequestBody ${entityType.typeName}CreateRequest createRequest);

@PostMapping("{id}/update")
R<${'Boolean'}> update${entityType.typeName}(@RequestBody ${entityType.typeName}UpdateRequest updateRequest, @PathVariable(value = "id") String id);

@PostMapping("{id}/valid")
R<${'Boolean'}> valid${entityType.typeName}(@PathVariable(value = "id") String id);

@PostMapping("{id}/invalid")
R<${'Boolean'}> invalid${entityType.typeName}(@PathVariable(value = "id") String id);

@PostMapping("query")
R<${'PageResult<${entityType.typeName}CommonResponse>'}> query${entityType.typeName}(
@RequestBody QueryRequest
<${'${entityType.typeName}QueryRequest'}> queryRequest);

@PostMapping("delete")
R<${'Boolean'}> delete${entityType.typeName}(@RequestBody List<${'String'}> ids);
}

class FeignInterceptor implements RequestInterceptor {
public FeignInterceptor() {
}

public void apply(RequestTemplate requestTemplate) {
requestTemplate.header("dapr-app-id", "${moduleName}");
}
}