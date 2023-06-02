package io.qifan.infrastructure.generator.processor.processor;


import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;


public interface ModelElementProcessor<P, R> {


    R process(ProcessorContext context, P sourceModel);

    int getPriority();

    interface ProcessorContext {
        Filer getFiler();

        TypeElement getTypeElement();
    }
}
