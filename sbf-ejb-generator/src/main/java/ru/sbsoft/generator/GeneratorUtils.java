package ru.sbsoft.generator;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import ru.sbsoft.shared.interfaces.ObjectType;
import ru.sbsoft.generator.api.Lookup;

public class GeneratorUtils {

    private GeneratorUtils() {
    }

    public static List<Element> findAllFields(Element formProcessor, Types types) {
        return findLookupFields(formProcessor, types, null);
    }

    private static List<Element> findLookupFields(Element classElement, Types types, List<Element> annotatedFieldList) {
        if (annotatedFieldList == null) {
            annotatedFieldList = new ArrayList<>();
        }

        for (final TypeMirror supertype : types.directSupertypes(classElement.asType())) {
            final Element superElement = types.asElement(supertype);
            findLookupFields(superElement, types, annotatedFieldList);
        }

        annotatedFieldList.addAll(classElement.getEnclosedElements());

        return annotatedFieldList;
    }

    public static String annotationForClass(String fullClassName) {
        switch (fullClassName) {
            case "javax.persistence.EntityManager":
                //return "javax.persistence.PersistenceContext(unitName = Const.DEFAULT_PERSISTENCE_CTX)";
                return "javax.persistence.PersistenceContext";
            case "javax.ejb.SessionContext":
            case "javax.mail.Session":
                return "javax.annotation.Resource";
            default:
                return "javax.ejb.EJB";
        }
    }

    public static InjectionInfo createSessionContextInjection() {
        InjectionInfo sessionContext = new InjectionInfo();
        sessionContext.setVarName("sessionContext");
        final String className = "javax.ejb.SessionContext";
        sessionContext.setClassName(className);
        sessionContext.setAnnotation(annotationForClass(className));
        return sessionContext;
    }

    public static String extractClassName(String fullClassName) {
        return fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
    }

    public static String variableName(String className) {
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    public static FieldAnalyseResult analyseLookupFields(final List<Element> annotatedFields) {
        final Set<LookupFieldInfo> fields = new TreeSet<>();
        final Set<InjectionInfo> injections = new TreeSet<>();

        for (final Element annotatedField : annotatedFields) {
            final Annotation annotation = annotatedField.getAnnotation(Lookup.class);
            if (null != annotation) {
                System.out.println("Create info for child element = " + annotatedField.getEnclosingElement().asType() + " " + annotatedField);

                final Lookup lookup = annotatedField.getAnnotation(Lookup.class);
                final String var_syffix = lookup.resourceName().isEmpty() ? "" : "_" + lookup.resourceName();

                String fullClassName = annotatedField.asType().toString();
                String className = GeneratorUtils.extractClassName(fullClassName);
                String varName = GeneratorUtils.variableName(className);

                final LookupFieldInfo fieldInfo = new LookupFieldInfo();
                fieldInfo.setVarName(varName + var_syffix);
                fieldInfo.setFieldName(annotatedField.getSimpleName().toString());
                fieldInfo.setClassName(removeGenerics(annotatedField.getEnclosingElement().asType().toString()));
                fields.add(fieldInfo);

                final InjectionInfo injectionInfo = new InjectionInfo();
                //injectionInfo.setVarName(varName);
                injectionInfo.setClassName(fullClassName);
                // injectionInfo.setAnnotation(GeneratorUtils.annotationForClass(fullClassName));

                injectionInfo.setResourceName(lookup.resourceName());

                injectionInfo.setVarName(varName + var_syffix);
                injectionInfo.setAnnotation(GeneratorUtils.annotationForClass(fullClassName));
                injections.add(injectionInfo);
            }
        }
        FieldAnalyseResult result = new FieldAnalyseResult();
        result.fields = fields;
        result.injections = injections;

        return result;
    }

    private static String removeGenerics(String string) {
        if (string == null) {
            return null;
        }
        return string.replaceAll("<[^>]*>", "");
    }

    public static class FieldAnalyseResult {

        Set<LookupFieldInfo> fields;
        Set<InjectionInfo> injections;

        public Set<LookupFieldInfo> getFields() {
            return fields;
        }

        public void setFields(Set<LookupFieldInfo> fields) {
            this.fields = fields;
        }

        public Set<InjectionInfo> getInjections() {
            return injections;
        }

        public void setInjections(Set<InjectionInfo> injections) {
            this.injections = injections;
        }
    }

    public static boolean isAnnotationAnnotatedWith(AnnotationMirror sourceAnnotation, Class annotatedClass) {
        final Element annotationElement = sourceAnnotation.getAnnotationType().asElement();
        if (annotationElement.asType().toString().startsWith("java")) {
            return false;
        }
        for (AnnotationMirror annotation : annotationElement.getAnnotationMirrors()) {
            if (annotationElement.getAnnotation(annotatedClass) != null
                    || isAnnotationAnnotatedWith(annotation, annotatedClass)) {
                return true;
            }
        }

        return false;
    }

    public static <T extends ObjectType> T readObjectType(Element sourceAnnotation, Messager messager) throws ClassNotFoundException {
        for (AnnotationMirror annotation : sourceAnnotation.getAnnotationMirrors()) {
            messager.printMessage(Diagnostic.Kind.NOTE, "checking annotation", annotation.getAnnotationType().asElement());
            final Enum result = readEnumFromAnnotation(annotation, "value", messager);
            if (result == null) {
                continue;
            }

            return (T) result;
        }
        return null;
    }

    public static Enum readEnumFromAnnotation(AnnotationMirror annotation, String fieldName, Messager messager) throws ClassNotFoundException {
        final Map<? extends ExecutableElement, ? extends AnnotationValue> annotationFields = annotation.getElementValues();
        for (ExecutableElement field : annotationFields.keySet()) {
            final TypeMirror returnType = field.getReturnType();
            // messager.printMessage(Diagnostic.Kind.NOTE, "searching " + fieldName + "() in " + field.getSimpleName() + ", returning type = '" + returnType + "'");
            if (field.getSimpleName().contentEquals(fieldName)) {
                final Class returnValueClass = Class.forName(returnType.toString());
                if (!Enum.class.isAssignableFrom(returnValueClass)) {
                    return null;
                }
                final List<Enum> enumFields = new ArrayList<>(EnumSet.allOf(returnValueClass));
                for (Enum e : enumFields) {
                    if (e.name().equals(annotationFields.get(field).getValue().toString())) {
                        messager.printMessage(Diagnostic.Kind.NOTE, "found " + fieldName + "() in " + field.getSimpleName() + ", returning type = '" + returnType + "'");

                        return e;
                    }
                }
            }
        }
        return null;
    }

    public static boolean readBoolFrommAnnotation(Element sourceAnnotation, String fieldName, ProcessingEnvironment processingEnv) throws ClassNotFoundException {

        final Messager messager = processingEnv.getMessager();
        //processingEnv.getElementUtils().getElementValuesWithDefaults(sourceAnnotation);

        for (AnnotationMirror annotation : sourceAnnotation.getAnnotationMirrors()) {

            final Map<? extends ExecutableElement, ? extends AnnotationValue> annotationFields = processingEnv.getElementUtils().getElementValuesWithDefaults(annotation);//annotation.getElementValuesWithDefaults();
            for (ExecutableElement field : annotationFields.keySet()) {
                /*final TypeMirror returnType = field.getReturnType();

                messager.printMessage(Diagnostic.Kind.NOTE, "searching " + fieldName + "() in " + field.getSimpleName() + ", returning type = '" + returnType + "'" + " defvalue = "
                        + (field.getDefaultValue() == null ? "" : field.getDefaultValue().getValue().toString()) );
                 */
                if (field.getSimpleName().contentEquals(fieldName) && field.getReturnType().getKind() == TypeKind.BOOLEAN) {
                    messager.printMessage(Diagnostic.Kind.NOTE, "found " + field.getSimpleName() + ", returning type = '" + field.getReturnType().getKind() + "'");

                    //final Class returnValueClass = Class.forName(returnType.toString());
                    //if (!Boolean.class.isAssignableFrom(returnValueClass)) {
                    //    return true;
                    //}
                    return (boolean) annotationFields.get(field).getValue();

                }
            }
        }
        return true;

    }

    public interface AddProcessorCallback {

        void onAddProcessor(Element processor, ObjectType processorType, ProcessorInfo processorInfo);
    }
//OperationProcessorAnnotation.class

    public static ProcessorAgregator createAgregatorForProcessorAnnotation(final Class<? extends Annotation> processorClass, final ProcessingEnvironment processingEnv, final RoundEnvironment roundEnv) {
        return createAgregatorForProcessorAnnotation(processorClass, processingEnv, roundEnv, null);
    }

    public static ProcessorAgregator createAgregatorForProcessorAnnotation(final Class<? extends Annotation> processorClass, final ProcessingEnvironment processingEnv, final RoundEnvironment roundEnv, final AddProcessorCallback callback) {
        final ProcessorAgregator agregator = new ProcessorAgregator(processingEnv);
        for (Element processorAnnotation : roundEnv.getElementsAnnotatedWith(processorClass)) {
            for (final Element processor : roundEnv.getElementsAnnotatedWith((TypeElement) processorAnnotation)) {
                try {
                    ObjectType operationType = readObjectType(processor, processingEnv.getMessager());
                    ProcessorInfo processorInfo = agregator.addProcessor(processor, operationType);

                    if (processorInfo != null) {
                        processorInfo.setTransaction(readBoolFrommAnnotation(processor, "inTransaction", processingEnv));

                    }

                    if (callback != null) {
                        callback.onAddProcessor(processor, operationType, processorInfo);
                    }
                } catch (ClassNotFoundException ex) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Невозможно обработать элемент", processor);
                    ex.printStackTrace();
                }
            }
        }
        return agregator;
    }
}
