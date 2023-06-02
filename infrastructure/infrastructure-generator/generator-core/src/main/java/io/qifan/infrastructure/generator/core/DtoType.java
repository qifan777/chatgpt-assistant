package io.qifan.infrastructure.generator.core;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.FIELD})
public @interface DtoType {
    String type() default "java.lang.String";

    boolean association() default false;
}
