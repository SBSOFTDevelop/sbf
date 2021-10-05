package ru.sbsoft.generator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.tools.Diagnostic;
import javax.validation.UnexpectedTypeException;
import ru.sbsoft.common.jdbc.Const;
import ru.sbsoft.shared.interfaces.ObjectType;
import ru.sbsoft.generator.api.DaoPersistenceContext;

public class ProcessorAgregator {

    private final Set<ProcessorInfo> processors = new HashSet<>();
    private final Set<String> processorTypes = new HashSet<>();
    private final Set<InjectionInfo> injections = new HashSet<>();
   

    private final Set<String> unitNames = new HashSet<>();

//
    private final ProcessingEnvironment processingEnv;

    public ProcessorAgregator(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    public ProcessorInfo addProcessor(Element processor, ObjectType processorType) {
        if (processorType == null) {
            return null;
        }
        processorTypes.add(processorType.getClass().getName());

        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing element", processor);

        switch (processor.getKind()) {
            case CLASS:
            case METHOD:
                break;
            default:
                throw new UnexpectedTypeException("CLASS, METHOD supports only (" + processor.asType() + ")");
        }

        final ProcessorInfo processorInfo = new ProcessorInfo();

        final String code = processorType.getCode();
        processorInfo.setCode(code);

        final String method = code.replaceAll("[\\s\\(\\)\\.\\-/]", "_");
        processorInfo.setCreateMethodName(method);

        Element operationElementClass = processor.getKind() == ElementKind.CLASS ? processor : processor.getEnclosingElement();
        processorInfo.setClassName(operationElementClass.asType().toString());

        if (processor.getKind() == ElementKind.CLASS) {

            final DaoPersistenceContext annotation = processor.getAnnotation(DaoPersistenceContext.class);
            if (annotation != null) {

                if (!annotation.value().isEmpty() && !Const.DEFAULT_PERSISTENCE_CTX.contentEquals(annotation.value())) {
                    unitNames.add(annotation.value());
                    processorInfo.setUnitName(annotation.value());

                    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Find GridPersistenceContext annotation", processor);
                }

            }
        }

        List<Element> lookupFields = GeneratorUtils.findAllFields(operationElementClass, processingEnv.getTypeUtils());
        GeneratorUtils.FieldAnalyseResult analyseResult = GeneratorUtils.analyseLookupFields(lookupFields);
        processorInfo.addFields(analyseResult.getFields());
        injections.addAll(analyseResult.getInjections());

        if (processor.getKind() == ElementKind.METHOD) {
            processorInfo.setPostConstructMethod(processor.getSimpleName().toString());
        }

        processors.add(processorInfo);
        return processorInfo;
    }

    public Set<ProcessorInfo> getProcessors() {
        return processors;
    }

    public Set<String> getProcessorTypes() {
        return processorTypes;
    }

    public Set<InjectionInfo> getInjections() {
        return injections;
    }

    public Set<String> getUnitNames() {
        return unitNames;
    }

}
