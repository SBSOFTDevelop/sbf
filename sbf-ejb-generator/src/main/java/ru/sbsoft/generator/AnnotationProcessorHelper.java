package ru.sbsoft.generator;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

/**
 *
 * @author Kiselev
 */
public class AnnotationProcessorHelper {

    private final ProcessingEnvironment processingEnv;
    private final RoundEnvironment roundEnv;

    public AnnotationProcessorHelper(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
        this.processingEnv = processingEnv;
        this.roundEnv = roundEnv;
    }

    public Set<? extends Element> findAnnotated(Class<? extends Annotation> a, String superClassFullName) {
        if (superClassFullName == null) {
            throw new IllegalArgumentException("superClassFullName can't be null");
        }
        TypeElement base = processingEnv.getElementUtils().getTypeElement(superClassFullName);
        if (base == null) {
            throw new RuntimeException(superClassFullName + " is not found");
        }
        TypeMirror baseType = base.asType();
        Types types = processingEnv.getTypeUtils();
        Set<Element> res = new HashSet<Element>();
        for (final Element e : roundEnv.getElementsAnnotatedWith(a)) {
            TypeMirror t = e.asType();
            if (types.isAssignable(t, baseType)) {
                res.add(e);
            }
        }
        return res;
    }

    public GeneratorUtils.FieldAnalyseResult getInjectFields(Element classElement) {
        return GeneratorUtils.analyseLookupFields(GeneratorUtils.findAllFields(classElement, processingEnv.getTypeUtils()));
    }

    public UtilClassInfo getUtilClass(Class<? extends Annotation> a, String baseClassName) {
        return getUtilClass(a, baseClassName, baseClassName);
    }

    public UtilClassInfo getUtilClass(Class<? extends Annotation> a, String baseClassName, String defaultClassName) {
        Set<? extends Element> contexts = findAnnotated(a, baseClassName);
        if (contexts.size() > 1) {
            throw new RuntimeException("Annotation " + a.getClass().getName() + " is applyed on more than one class");
        }
        if (contexts.isEmpty()) {
            if (defaultClassName != null) {
                return new UtilClassInfo(defaultClassName, Collections.<LookupFieldInfo>emptySet(), Collections.<InjectionInfo>emptySet());
            } else {
                return null;
            }
        } else {
            Element c = contexts.iterator().next();
            String globalContext = c.toString();
            GeneratorUtils.FieldAnalyseResult globalContextInjectFields = getInjectFields(c);
            return new UtilClassInfo(globalContext, globalContextInjectFields.getFields(), globalContextInjectFields.getInjections());
        }
    }
}
