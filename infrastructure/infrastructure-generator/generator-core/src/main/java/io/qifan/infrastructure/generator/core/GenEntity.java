package io.qifan.infrastructure.generator.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface GenEntity {
    String sourcePath() default "src/main/java";

    String requestSourcePath() default "src/main/java";

    String responseSourcePath() default "src/main/java";

    GenType[] genTypeList() default {GenType.DTO_QUERY,
                                     GenType.DTO_CREATE,
                                     GenType.DTO_UPDATE,
                                     GenType.DTO_RESPONSE,
                                     GenType.MAPPER,
                                     GenType.REPOSITORY,
                                     GenType.SERVICE,
                                     GenType.CONTROLLER,
                                     GenType.TYPING,
                                     GenType.FEIGN,
                                     GenType.API};

    GenController genController() default @GenController;

    GenService genService() default @GenService;

    GenMapper genMapper() default @GenMapper;

    GenRepository genRepository() default @GenRepository;

    GenRequest[] genRequest() default {
            @GenRequest(suffix = "CreateRequest"),
            @GenRequest(suffix = "UpdateRequest"),
            @GenRequest(suffix = "QueryRequest")};

    GenResponse[] genResponse() default {
            @GenResponse(suffix = "CommonResponse")
    };
    GenFeign genFeign()default @GenFeign;


}
