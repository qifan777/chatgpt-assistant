package io.qifan.infrastructure.generator.core;

public @interface GenFeign {
    String moduleName() default "";
    String sourcePath() default "";

    String suffix() default "FeignClient";

    String packageName() default "feign";
}
