package io.qifan.infrastructure.generator.processor.processor;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

public class DefaultModelElementProcessorContext implements ModelElementProcessor.ProcessorContext {
    private final ProcessingEnvironment processingEnvironment;
    private final TypeElement typeElement;

    public DefaultModelElementProcessorContext(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {
        this.processingEnvironment = processingEnvironment;
        this.typeElement = typeElement;
    }

    @Override
    public Filer getFiler() {
        return processingEnvironment.getFiler();
    }

    @Override
    public TypeElement getTypeElement() {
        return typeElement;
    }
}
