package io.qifan.infrastructure.generator.core;

public @interface GenRequest {
    String sourcePath() default "";

    String suffix() default "Request";

    String packageName() default "dto.request";

    String[] ignoreFields() default {};
}
