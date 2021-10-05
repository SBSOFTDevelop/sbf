package ru.sbsoft.generator;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import ru.sbsoft.generator.api.TreeProcessorAnnotation;

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
@SupportedAnnotationTypes("ru.sbsoft.generator.api.TreeProcessorAnnotation")
public class TreeManagerGenerator extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }

        final String optionsPckg = processingEnv.getOptions().get("package");
        final String pckg = ("ru.sbsoft." + (optionsPckg == null ? "common" : optionsPckg)).toLowerCase().replaceAll("-", ".");

        final ProcessorAgregator agregator = GeneratorUtils.createAgregatorForProcessorAnnotation(TreeProcessorAnnotation.class, processingEnv, roundEnv);
        final Set<String> unitNames = agregator.getUnitNames();
        try {
            final String genClassName = "TreeManager";
            final VelocityContext context = new VelocityContext();
            context.put("genClassName", genClassName);
            context.put("trees", agregator.getProcessors());
            context.put("treeTypes", agregator.getProcessorTypes());
            context.put("injections", agregator.getInjections());
            context.put("now", new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date()));
            context.put("package", pckg);
            context.put("unitTemplates", unitNames);
//
            final JavaFileObject outFile = processingEnv.getFiler().createSourceFile(pckg + "." + genClassName);
            VelocityUtils.createSingleFile(outFile, "treeManager.vm", context);
        } catch (Exception ex) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "TreeManagerGenerator error " + ex.getMessage());
            ex.printStackTrace();
        }

        return false;
    }

}
