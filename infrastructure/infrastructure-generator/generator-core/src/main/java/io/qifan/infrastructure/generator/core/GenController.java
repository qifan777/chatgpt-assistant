package io.qifan.infrastructure.generator.core;

public @interface GenController {
    String sourcePath() default "";

    String suffix() default "Controller";

    String packageName() default "controller";
}
