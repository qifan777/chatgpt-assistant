package io.qifan.infrastructure.generator.processor.processor;


import io.qifan.infrastructure.generator.core.*;
import io.qifan.infrastructure.generator.processor.model.Entity;
import io.qifan.infrastructure.generator.processor.model.common.Field;
import io.qifan.infrastructure.generator.processor.model.controller.Controller;
import io.qifan.infrastructure.generator.processor.model.dto.Request;
import io.qifan.infrastructure.generator.processor.model.dto.Response;
import io.qifan.infrastructure.generator.processor.model.feign.Feign;
import io.qifan.infrastructure.generator.processor.model.front.Api;
import io.qifan.infrastructure.generator.processor.model.front.Typing;
import io.qifan.infrastructure.generator.processor.model.front.TypingField;
import io.qifan.infrastructure.generator.processor.model.mapper.Mapper;
import io.qifan.infrastructure.generator.processor.model.repository.Repository;
import io.qifan.infrastructure.generator.processor.model.service.Service;
import io.qifan.infrastructure.generator.processor.processor.front.mapping.*;
import io.qifan.infrastructure.generator.processor.utils.FieldUtils;
import io.qifan.infrastructure.generator.processor.utils.GenEntityUtils;
import io.qifan.infrastructure.generator.processor.utils.TypeUtils;
import org.springframework.util.StringUtils;

import javax.lang.model.element.TypeElement;
import java.util.List;

public class EntityCreateProcessor implements ModelElementProcessor<Void, Entity> {
    private static final List<TypeMapping> typeMappings;

    static {
        typeMappings = List.of(new ArrayMapping(), new BooleanMapping(), new NumberMapping(),
                               new StringMapping(), new ValidStatusMapping(), new ObjectMapping());
    }

    private TypeElement typeElement;
    private GenEntityUtils genEntityUtils;

    @Override
    public Entity process(ProcessorContext processorContext, Void sourceModel) {
        typeElement = processorContext.getTypeElement();
        genEntityUtils = new GenEntityUtils(typeElement.getAnnotation(GenEntity.class));
        return Entity.builder()
                     .type(TypeUtils.getType(typeElement))
                     .fields(FieldUtils.getFields(typeElement))
                     .requests(createRequest())
                     .responses(createResponse())
                     .mapper(createMapper())
                     .repository(createRepository())
                     .service(createService())
                     .controller(createController())
                     .feign(createFeign())
                     .typing(typing())
                     .api(api())
                     .build();
    }

    private Service createService() {
        if (!genEntityUtils.hasService()) {
            return null;
        }
        GenService service = genEntityUtils.getService();
        return Service.builder()
                      .sourcePath(StringUtils.hasText(
                              service.sourcePath()) ? service.sourcePath() : genEntityUtils.getSourcePath())
                      .type(TypeUtils.getType(typeElement, service.packageName(), service.suffix()))
                      .entityType(TypeUtils.getType(typeElement))
                      .build();
    }


    private Controller createController() {
        if (!genEntityUtils.hasController()) {
            return null;
        }
        GenController controller = genEntityUtils.getController();
        return Controller.builder()
                         .sourcePath(StringUtils.hasText(
                                 controller.sourcePath()) ? controller.sourcePath() : genEntityUtils.getSourcePath())
                         .type(TypeUtils.getType(typeElement, controller.packageName(), controller.suffix()))
                         .entityType(TypeUtils.getType(typeElement))
                         .build();
    }

    private Repository createRepository() {
        if (!genEntityUtils.hasRepository()) {
            return null;
        }
        GenRepository repository = genEntityUtils.getRepository();
        return Repository.builder()
                         .sourcePath(StringUtils.hasText(
                                 repository.sourcePath()) ? repository.sourcePath() : genEntityUtils.getSourcePath())
                         .type(TypeUtils.getType(typeElement, repository.packageName(), repository.suffix()))
                         .entityType(TypeUtils.getType(typeElement))
                         .build();
    }


    private Mapper createMapper() {
        if (!genEntityUtils.hasMapper()) {
            return null;
        }
        GenMapper mapper = genEntityUtils.getMapper();
        return Mapper.builder()
                     .sourcePath(StringUtils.hasText(
                             mapper.sourcePath()) ? mapper.sourcePath() : genEntityUtils.getSourcePath())
                     .entityType(TypeUtils.getType(typeElement))
                     .type(TypeUtils.getType(typeElement, mapper.packageName(), mapper.suffix()))
                     .build();
    }

    private List<Response> createResponse() {
        List<GenResponse> responses = genEntityUtils.getResponses();
        return responses.stream().map(response -> Response.builder()
                                                          .sourcePath(StringUtils.hasText(
                                                                  response.sourcePath()) ? response.sourcePath() : genEntityUtils.getResponseSourcePath())
                                                          .type(TypeUtils.getType(typeElement,
                                                                                  response.packageName(),
                                                                                  response.suffix()))
                                                          .entityType(TypeUtils.getType(typeElement))
                                                          .fields(FieldUtils.getFieldsForResponse(typeElement,
                                                                                                  response.ignoreFields(),
                                                                                                  response.packageName(),
                                                                                                  response.suffix()))
                                                          .build()).toList();
    }

    private List<Request> createRequest() {
        List<GenRequest> requests = genEntityUtils.getRequests();
        return requests.stream().map(request -> Request.builder()
                                                       .sourcePath(StringUtils.hasText(
                                                               request.sourcePath()) ? request.sourcePath() : genEntityUtils.getRequestSourcePath())
                                                       .type(TypeUtils.getType(typeElement,
                                                                               request.packageName(),
                                                                               request.suffix()))
                                                       .entityType(TypeUtils.getType(typeElement))
                                                       .fields(FieldUtils.getFieldsForRequest(typeElement,
                                                                                              request.ignoreFields(),
                                                                                              request.packageName(),
                                                                                              request.suffix()))
                                                       .build()).toList();
    }

    private Feign createFeign() {
        GenFeign feign = genEntityUtils.getFeign();
        if (!StringUtils.hasText(feign.moduleName())) return null;
        return Feign.builder()
                    .sourcePath(StringUtils.hasText(
                            feign.sourcePath()) ? feign.sourcePath() : genEntityUtils.getSourcePath())
                    .moduleName(feign.moduleName())
                    .type(TypeUtils.getType(typeElement, feign.packageName(), feign.suffix()))
                    .entityType(TypeUtils.getType(typeElement))
                    .build();
    }

    private Typing typing() {
        if (!genEntityUtils.hasTyping()) return null;
        List<Field> fields = FieldUtils.getFields(typeElement);
        List<TypingField> typingFields = fields.stream().map(field -> {
            for (TypeMapping typeMapping : typeMappings) {
                String convert = typeMapping.convert(field);
                if (convert != null) {
                    // 字段名称+ts类型
                    return new TypingField(field.getFieldName(), convert);
                }
            }
            return null;
        }).toList();
        return Typing.builder()
                     .entityType(TypeUtils.getType(typeElement))
                     .typingFields(typingFields).build();
    }

    private Api api() {
        if (!genEntityUtils.hasApi()) return null;
        return Api.builder().entityType(TypeUtils.getType(typeElement)).build();
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
