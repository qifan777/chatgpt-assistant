package io.qifan.infrastructure.generator.core;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.FIELD})
public @interface GenField {
    String description() default "";

    String dtoType() default "";

    boolean association() default false;

    boolean ignoreRequest() default false;

    boolean ignoreResponse() default false;

}
