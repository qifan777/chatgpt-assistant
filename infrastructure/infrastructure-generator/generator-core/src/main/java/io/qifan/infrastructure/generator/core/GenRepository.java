package io.qifan.infrastructure.generator.core;


public @interface GenRepository {
    String sourcePath() default "";

    String packageName() default "repository";

    String suffix() default "Repository";
}
