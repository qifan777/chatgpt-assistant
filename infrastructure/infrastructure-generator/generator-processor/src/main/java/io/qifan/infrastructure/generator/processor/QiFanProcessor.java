package io.qifan.infrastructure.generator.processor;


import io.qifan.infrastructure.generator.processor.processor.DefaultModelElementProcessorContext;
import io.qifan.infrastructure.generator.processor.processor.EntityCreateProcessor;
import io.qifan.infrastructure.generator.processor.processor.ModelElementProcessor;
import io.qifan.infrastructure.generator.processor.processor.TemplateRenderingProcessor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@SupportedAnnotationTypes("io.qifan.infrastructure.generator.core.GenEntity")
public class QiFanProcessor extends AbstractProcessor {
    Set<ModelElementProcessor<?, ?>> processors;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        processors = new TreeSet<>((o1, o2) -> o2.getPriority() - o1.getPriority());
        processors.add(new TemplateRenderingProcessor());
        processors.add(new EntityCreateProcessor());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<TypeElement> entities = getEntities(annotations, roundEnv);
        processEntityElements(entities);
        return false;
    }

    public Set<TypeElement> getEntities(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        Set<TypeElement> entityTypes = new HashSet<>();
        for (TypeElement annotation : annotations) {
            if (annotation.getKind() != ElementKind.ANNOTATION_TYPE) {
                continue;
            }
            Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(annotation);
            Set<TypeElement> typeElements = ElementFilter.typesIn(elementsAnnotatedWith);
            entityTypes.addAll(typeElements);

        }
        return entityTypes;
    }

    public void processEntityElements(Set<TypeElement> typeElements) {
        for (TypeElement typeElement : typeElements) {
            DefaultModelElementProcessorContext defaultModelElementProcessorContext =
                    new DefaultModelElementProcessorContext(
                            this.processingEnv,
                            typeElement);
            processEntityTypeElement(defaultModelElementProcessorContext);
        }
    }

    private void processEntityTypeElement(ModelElementProcessor.ProcessorContext context) {
        Object model = null;
        for (ModelElementProcessor<?, ?> processor : processors) {
            model = process(context, processor, model);
        }
    }

    private <P, R> R process(ModelElementProcessor.ProcessorContext context, ModelElementProcessor<P, R> processor,
                             Object modelElement) {
        @SuppressWarnings("unchecked")
        P sourceElement = (P) modelElement;
        return processor.process(context, sourceElement);
    }

}
