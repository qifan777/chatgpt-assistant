package io.qifan.infrastructure.generator.processor.utils;

import javax.annotation.processing.Messager;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public record AnnotationProcessorContext(Elements elementUtils, Types typeUtils, Messager messager) {

}
