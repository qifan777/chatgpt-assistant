package io.qifan.infrastructure.generator.core;

public @interface GenService {
    String sourcePath() default "";

    String packageName() default "service";

    String suffix() default "Service";
}
