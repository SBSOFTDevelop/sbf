package ru.sbsoft.generator;

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import ru.sbsoft.generator.api.FormProcessor;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("ru.sbsoft.generator.api.FormProcessor")
public class FormDaoBeanProcessor extends AbstractProcessor {

    public final static String INTERFACE_CLASS = "ru.sbsoft.form.IFormProcessor";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }
        for (final Element element : roundEnv.getElementsAnnotatedWith(FormProcessor.class)) {
            if (isWrongInherited(element.asType())) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        "Класс должен реализовывать " + INTERFACE_CLASS,
                        element);
                return true;
            }
        }
        return true;
    }

    private boolean isWrongInherited(final TypeMirror typeMirror) {
        final Types types = processingEnv.getTypeUtils();
        for (final TypeMirror supertype : types.directSupertypes(typeMirror)) {
            if (supertype.toString().startsWith(INTERFACE_CLASS) || !isWrongInherited(supertype)) {
                return false;
            }
        }
        return true;
    }
}
