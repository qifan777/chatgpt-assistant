package io.qifan.infrastructure.generator.processor.processor;


import io.qifan.infrastructure.generator.processor.model.Entity;
import io.qifan.infrastructure.generator.processor.model.controller.Controller;
import io.qifan.infrastructure.generator.processor.model.dto.Request;
import io.qifan.infrastructure.generator.processor.model.dto.Response;
import io.qifan.infrastructure.generator.processor.model.feign.Feign;
import io.qifan.infrastructure.generator.processor.model.front.Api;
import io.qifan.infrastructure.generator.processor.model.front.Typing;
import io.qifan.infrastructure.generator.processor.model.mapper.Mapper;
import io.qifan.infrastructure.generator.processor.model.repository.Repository;
import io.qifan.infrastructure.generator.processor.model.service.Service;
import io.qifan.infrastructure.generator.processor.writer.ModelWriter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class TemplateRenderingProcessor implements ModelElementProcessor<Entity, Entity> {
    @Override
    public Entity process(ProcessorContext processorContext, Entity entity) {
        ModelWriter modelWriter = new ModelWriter();
        if (entity.hasController()) {
            writeController(modelWriter, entity.getController());
        }
        if (entity.hasRequests()) {
            writeRequests(modelWriter, entity.getRequests());
        }
        if (entity.hasResponse()) {
            writeResponse(modelWriter, entity.getResponses());
        }
        if (entity.hasMapper()) {
            writeMapper(modelWriter, entity.getMapper());
        }
        if (entity.hasRepository()) {
            writeRepository(modelWriter, entity.getRepository());
        }
        if (entity.hasService()) {
            writeService(modelWriter, entity.getService());
        }
        if (entity.hasFeign()) {
            writeFeign(modelWriter, entity.getFeign());
        }
        if (entity.hasTyping()) {
            writeTyping(modelWriter, entity.getTyping());
        }
        if (entity.hasApi()) {
            writeApi(modelWriter, entity.getApi());
        }
        return entity;
    }

    private void writeApi(ModelWriter modelWriter, Api api) {
        modelWriter.writeModel(api, false);
    }

    private void writeTyping(ModelWriter modelWriter, Typing typing) {
        modelWriter.writeModel(typing, true);
    }

    @SneakyThrows
    public void writeController(ModelWriter modelWriter,
                                Controller controller) {

        modelWriter.writeModel(controller.getSourcePath(), controller.getType(), controller);
    }

    public void writeService(ModelWriter modelWriter, Service service) {
        modelWriter.writeModel(service.getSourcePath(), service.getType(), service);
    }

    public void writeRepository(ModelWriter modelWriter, Repository repository) {
        modelWriter.writeModel(repository.getSourcePath(), repository.getType(), repository);
    }

    public void writeMapper(ModelWriter modelWriter, Mapper mapper) {
        modelWriter.writeModel(mapper.getSourcePath(), mapper.getType(), mapper);
    }


    public void writeRequests(ModelWriter modelWriter, List<Request> requests) {
        requests.forEach(request -> {
            modelWriter.writeModel(request.getSourcePath(), request.getType(), request);
        });
    }

    public void writeResponse(ModelWriter modelWriter, List<Response> responses) {
        responses.forEach(response -> {
            modelWriter.writeModel(response.getSourcePath(), response.getType(), response);
        });
    }

    public void writeFeign(ModelWriter modelWriter, Feign feign) {
        modelWriter.writeModel(feign.getSourcePath(), feign.getType(), feign);
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
