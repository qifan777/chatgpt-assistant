package io.qifan.infrastructure.generator.processor.model;


import io.qifan.infrastructure.generator.processor.model.common.Field;
import io.qifan.infrastructure.generator.processor.model.common.ModelElement;
import io.qifan.infrastructure.generator.processor.model.common.Type;
import io.qifan.infrastructure.generator.processor.model.controller.Controller;
import io.qifan.infrastructure.generator.processor.model.dto.Request;
import io.qifan.infrastructure.generator.processor.model.dto.Response;
import io.qifan.infrastructure.generator.processor.model.feign.Feign;
import io.qifan.infrastructure.generator.processor.model.front.Api;
import io.qifan.infrastructure.generator.processor.model.front.Typing;
import io.qifan.infrastructure.generator.processor.model.mapper.Mapper;
import io.qifan.infrastructure.generator.processor.model.repository.Repository;
import io.qifan.infrastructure.generator.processor.model.service.Service;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class Entity extends ModelElement {
    private Type type;
    private List<Field> fields;
    private Controller controller;
    private List<Request> requests;
    private List<Response> responses;
    private Mapper mapper;
    private Repository repository;
    private Service service;
    private Feign feign;
    private Typing typing;
    private Api api;

    public boolean hasApi() {
        return api != null;
    }

    public boolean hasTyping() {
        return typing != null;
    }

    public boolean hasService() {
        return service != null;
    }

    public boolean hasRepository() {
        return repository != null;
    }

    public boolean hasRequests() {
        return !CollectionUtils.isEmpty(requests);
    }

    public boolean hasResponse() {
        return !CollectionUtils.isEmpty(responses);
    }

    public boolean hasController() {
        return controller != null;
    }

    public boolean hasMapper() {
        return mapper != null;
    }

    public boolean hasFeign() {
        return feign != null;
    }

    @Override
    public Set<Type> getImportTypes() {
        return null;
    }
}
