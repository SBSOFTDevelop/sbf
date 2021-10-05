package ru.sbsoft.generator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import org.apache.velocity.VelocityContext;
import ru.sbsoft.generator.api.DynFormProcessorAnnotation;
import ru.sbsoft.generator.api.FormProcessorAnnotation;

/**
 * Генератор менеджера обработчиков форм в проектах СБСофт. Для успешного
 * формирования менеджера с обработчиками должны быть выполнены несколько
 * условий:
 * <ul>
 * <li>созданы прикладные перечисления для форм, расширяющие FormType</li>
 * <li>созданы прикладные аннотации для каждого созданного перечисления</li>
 * <li>аннотации помечены ru.sbsoft.generator.api.FormProcessorAnnotation</li>
 * </ul>
 * Для системных перечислений также должны быть реализованы аннотации в каждом
 * использующем их проекте.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("ru.sbsoft.generator.api.FormProcessorAnnotation")
public class FormManagerGenerator extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }

        final String optionsPckg = processingEnv.getOptions().get("package");
        final String pckg = ("ru.sbsoft." + (optionsPckg == null ? "common" : optionsPckg)).toLowerCase().replaceAll("-", ".");

        final ProcessorAgregator agregator = GeneratorUtils.createAgregatorForProcessorAnnotation(FormProcessorAnnotation.class, processingEnv, roundEnv);
        final ProcessorAgregator dynagregator = GeneratorUtils.createAgregatorForProcessorAnnotation(DynFormProcessorAnnotation.class, processingEnv, roundEnv);
        
        final Set<InjectionInfo> injections = new HashSet<>();
        injections.addAll(agregator.getInjections());
        injections.addAll(dynagregator.getInjections());
        
        final Set<String> processorTypes = new HashSet<>();
        processorTypes.addAll(agregator.getProcessorTypes());
        processorTypes.addAll(dynagregator.getProcessorTypes());

        
        final Set<String> unitNames = agregator.getUnitNames();
        unitNames.addAll(dynagregator.getUnitNames());
        try {
            final VelocityContext context = new VelocityContext();
            context.put("forms", agregator.getProcessors());
            context.put("dynforms", dynagregator.getProcessors());
            context.put("formTypes", processorTypes);
            context.put("injections", injections);
            context.put("now", new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date()));
            context.put("package", pckg);
            
            context.put("unitTemplates", unitNames);
            //
            final JavaFileObject outFile = processingEnv.getFiler().createSourceFile(pckg + ".FormManager");
            VelocityUtils.createSingleFile(outFile, "formManager.vm", context);
        } catch (Exception ex) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "FormManagerGenerator error " + ex.getMessage());
            ex.printStackTrace();
        }

        return false;
    }
}
