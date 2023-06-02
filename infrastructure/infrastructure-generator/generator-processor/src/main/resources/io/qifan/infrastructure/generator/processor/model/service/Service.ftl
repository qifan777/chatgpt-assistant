<#-- @ftlvariable name="" type="io.qifan.infrastructure.generator.processor.model.service.Service" -->
package ${type.packagePath};

<#list importTypes as importType>
    import ${importType.getTypePath()};
</#list>
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.Predicate;

<#assign uncapitalizeTypeName = entityType.getUncapitalizeTypeName()>
@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ${entityType.getTypeName()}Service {
private final ${entityType.getTypeName()}Repository ${uncapitalizeTypeName}Repository;
private final ${entityType.getTypeName()}Mapper ${uncapitalizeTypeName}Mapper;

public ${entityType.getTypeName()}CommonResponse findById(String id) {
return ${uncapitalizeTypeName}Mapper.entity2Response(${uncapitalizeTypeName}Repository
.findById(id)
.orElseThrow(() -> new BusinessException(
ResultCode.NotFindError)));
}

public void update${entityType.getTypeName()}(${entityType.getTypeName()}UpdateRequest request, String id) {
EntityOperations.doUpdate(${uncapitalizeTypeName}Repository)
.loadById(id)
.update(e -> {
${uncapitalizeTypeName}Mapper.updateEntityFromUpdateRequest(request, e);
e.valid();
})
.successHook(e -> log.info("更新${entityType.getTypeName()}：{}", e))
.execute();
}

public String create${entityType.getTypeName()}(${entityType.getTypeName()}CreateRequest request) {
Optional<${entityType.getTypeName()}> ${uncapitalizeTypeName} = EntityOperations.doCreate(${uncapitalizeTypeName}Repository)
.create(() -> ${uncapitalizeTypeName}Mapper.createRequest2Entity(
request))
.update(${entityType.getTypeName()}::valid)
.successHook(e -> {
log.info("创建${entityType.getTypeName()}：{}", e);

})
.execute();
return ${uncapitalizeTypeName}.map(${entityType.getTypeName()}::getId).orElse(null);
}

public void valid${entityType.getTypeName()}(String id) {
EntityOperations.doUpdate(${uncapitalizeTypeName}Repository)
.loadById(id)
.update(${entityType.getTypeName()}::valid)
.successHook(e -> log.info("生效${entityType.getTypeName()}：{}", e))
.execute();
}

public void invalid${entityType.getTypeName()}(String id) {
EntityOperations.doUpdate(${uncapitalizeTypeName}Repository)
.loadById(id)
.update(${entityType.getTypeName()}::invalid)
.successHook(e -> log.info("失效${entityType.getTypeName()}：{}", e))
.execute();
}

public Page<${entityType.getTypeName()+'CommonResponse'}> query${entityType.getTypeName()}(QueryRequest<${entityType.getTypeName()+'QueryRequest'}> request) {
ExampleMatcher matcher = ExampleMatcher.matchingAll();
Example<${entityType.getTypeName()}> example = Example.of(${uncapitalizeTypeName}Mapper.queryRequest2Entity(request.getQuery()), matcher);
Page<${entityType.getTypeName()}> page = ${uncapitalizeTypeName}Repository.findAll(example, request.toPageable());
return page.map(${uncapitalizeTypeName}Mapper::entity2Response);
}
public Boolean delete${entityType.getTypeName()}(List${'<String>'} ids) {
${uncapitalizeTypeName}Repository.deleteAllById(ids);
return true;
}
}