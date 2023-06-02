package io.qifan.infrastructure.generator.core;

public @interface GenResponse {
    String sourcePath() default "";

    String suffix() default "Response";

    String packageName() default "dto.response";

    String[] ignoreFields() default {};
}
