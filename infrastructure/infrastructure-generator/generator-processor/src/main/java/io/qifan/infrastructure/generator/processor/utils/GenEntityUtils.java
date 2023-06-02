package io.qifan.infrastructure.generator.processor.utils;


import io.qifan.infrastructure.generator.core.*;

import java.util.Arrays;
import java.util.List;

public class GenEntityUtils {
    private final GenEntity genEntity;

    public GenEntityUtils(GenEntity genEntity) {
        this.genEntity = genEntity;
    }

    public GenController getController() {
        return genEntity.genController();
    }

    public GenMapper getMapper() {
        return genEntity.genMapper();
    }

    public GenService getService() {
        return genEntity.genService();
    }

    public GenRepository getRepository() {
        return genEntity.genRepository();
    }

    public List<GenResponse> getResponses() {
        return List.of(genEntity.genResponse());
    }

    public List<GenRequest> getRequests() {
        return List.of(genEntity.genRequest());
    }
    public GenFeign getFeign() {
        return genEntity.genFeign();
    };

    public String getSourcePath() {
        return genEntity.sourcePath();
    }

    public String getRequestSourcePath() {
        return genEntity.requestSourcePath();
    }

    public String getResponseSourcePath() {
        return genEntity.responseSourcePath();
    }

    public boolean hasTyping() {
        return matchGenType(GenType.TYPING);
    }

    public boolean hasApi() {
        return matchGenType(GenType.API);
    }

    public boolean hasController() {
        return matchGenType(GenType.CONTROLLER);
    }

    public boolean hasService() {
        return matchGenType(GenType.SERVICE);
    }

    public boolean hasRepository() {
        return matchGenType(GenType.REPOSITORY);
    }

    public boolean hasMapper() {
        return matchGenType(GenType.MAPPER);
    }

    public boolean hasCreateDTO() {
        return matchGenType(GenType.DTO_CREATE);
    }

    public boolean hasUpdateDTO() {
        return matchGenType(GenType.DTO_UPDATE);
    }

    public boolean hasQueryDTO() {
        return matchGenType(GenType.DTO_QUERY);
    }

    public boolean hashResponseDTO() {
        return matchGenType(GenType.DTO_RESPONSE);
    }

    public boolean matchGenType(GenType genType) {
        return Arrays.asList(genEntity.genTypeList()).contains(genType);
    }

}
