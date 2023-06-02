package io.qifan.infrastructure.generator.core;


public @interface GenMapper {
    String sourcePath() default "";

    String packageName() default "mapper";

    String suffix() default "Mapper";

}
