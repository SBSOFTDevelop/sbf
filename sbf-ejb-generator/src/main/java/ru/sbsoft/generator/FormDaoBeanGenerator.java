package ru.sbsoft.generator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import org.apache.velocity.VelocityContext;
import ru.sbsoft.generator.GeneratorUtils.FieldAnalyseResult;
import ru.sbsoft.generator.api.FormProcessor;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes("ru.sbsoft.generator.api.FormProcessor")
public class FormDaoBeanGenerator extends AbstractProcessor {

    public FormDaoBeanGenerator() {
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }

        final List<FormProcessorInfo> forms = new ArrayList<>();
        final Set<String> rights = new TreeSet<>();
        final Set<InjectionInfo> injections = new TreeSet<>();
        injections.add(GeneratorUtils.createSessionContextInjection());

        for (final Element formProcessorElement : roundEnv.getElementsAnnotatedWith(FormProcessor.class)) {
            System.out.println("FP Create info for element = " + formProcessorElement.toString());
            final FormProcessorInfo formProcessorInfo = new FormProcessorInfo();
            final FormProcessor formProcessorAnnotation = formProcessorElement.getAnnotation(FormProcessor.class);
            if (formProcessorAnnotation == null) {
                continue;
            }
            formProcessorInfo.setFormType(formProcessorAnnotation.value());
            String rightsString = formProcessorAnnotation.rights();
            rightsString = (rightsString.equals("") ? formProcessorAnnotation.value() : formProcessorAnnotation.rights()) + ".MODIFY";
            formProcessorInfo.setFormRights(rightsString);
            rights.add(rightsString);
            formProcessorInfo.setClassName(formProcessorElement.asType().toString());

            List<Element> lookupFields = GeneratorUtils.findAllFields(formProcessorElement, processingEnv.getTypeUtils());
            FieldAnalyseResult analyseResult = GeneratorUtils.analyseLookupFields(lookupFields);
            formProcessorInfo.addFields(analyseResult.fields);
            injections.addAll(analyseResult.injections);
            forms.add(formProcessorInfo);
        }
        rights.remove("");

        if (forms.isEmpty()) {
            return false;
        }
        try {
            final VelocityContext context = new VelocityContext();
            context.put("forms", forms);
            context.put("rights", rights);
            context.put("injections", injections);
            context.put("now", new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date()));
            //
            final JavaFileObject outFile = processingEnv.getFiler().createSourceFile("ru.sbsoft.common.CommonFormDaoBean");
            VelocityUtils.createSingleFile(outFile, "formDaoBean.wm", context);
        } catch (Exception ex) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "FormDaoBeanGenerator error " + ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }

}
