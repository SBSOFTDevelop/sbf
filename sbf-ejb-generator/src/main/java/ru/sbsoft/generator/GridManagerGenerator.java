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
import ru.sbsoft.generator.api.grid.AppGlobalQueryContext;
import ru.sbsoft.generator.api.grid.AppSQLBuilder;
import ru.sbsoft.generator.api.grid.AppTemplateBuilder;
import ru.sbsoft.generator.api.grid.AppTemplateManager;
import ru.sbsoft.generator.api.grid.DynGridTemplateAnnotation;
import ru.sbsoft.generator.api.grid.GridTemplateAnnotation;

/**
 * Генератор менеджера обработчиков браузеров в проектах СБСофт. Для успешного
 * формирования менеджера с обработчиками должны быть выполнены несколько
 * условий:
 * <ul>
 * <li>созданы прикладные перечисления для форм, расширяющие GridType</li>
 * <li>созданы прикладные аннотации для каждого созданного перечисления</li>
 * <li>аннотации помечены
 * ru.sbsoft.generator.api.grid.GridProcessorAnnotation</li>
 * </ul>
 * Для системных перечислений также должны быть реализованы аннотации в каждом
 * использующем их проекте.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"ru.sbsoft.generator.api.grid.GridTemplateAnnotation", "ru.sbsoft.generator.api.grid.GridPersistenceContext", "ru.sbsoft.generator.api.grid.DynGridTemplateAnnotation", "ru.sbsoft.generator.api.grid.AppGlobalQueryContext", "ru.sbsoft.generator.api.grid.AppSQLBuilder", "ru.sbsoft.generator.api.grid.AppTemplateBuilder", "ru.sbsoft.generator.api.grid.AppTemplateManager"})
public class GridManagerGenerator extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }
        AnnotationProcessorHelper h = new AnnotationProcessorHelper(processingEnv, roundEnv);
        UtilClassInfo gc = h.getUtilClass(AppGlobalQueryContext.class, "ru.sbsoft.meta.context.GlobalQueryContext");
        UtilClassInfo sb = h.getUtilClass(AppSQLBuilder.class, "ru.sbsoft.meta.sql.SQLBuilder");
        UtilClassInfo tb = h.getUtilClass(AppTemplateBuilder.class, "ru.sbsoft.dao.DefaultTemplateBuilder");

        final ProcessorAgregator agregator = GeneratorUtils.createAgregatorForProcessorAnnotation(GridTemplateAnnotation.class, processingEnv, roundEnv);
        final ProcessorAgregator dynagregator = GeneratorUtils.createAgregatorForProcessorAnnotation(DynGridTemplateAnnotation.class, processingEnv, roundEnv);
        //final ProcessorAgregator gridctxagregator = GeneratorUtils.createAgregatorForProcessorAnnotation(GridPersistenceContext.class, processingEnv, roundEnv);

        Set<InjectionInfo> injections = new HashSet<>();
        injections.addAll(agregator.getInjections());
        injections.addAll(gc.getInjections());
        injections.addAll(sb.getInjections());
        injections.addAll(tb.getInjections());

        UtilClassInfo appTemplateManager = h.getUtilClass(AppTemplateManager.class, "ru.sbsoft.dao.ITemplateManager", null);

        try {
            final String optionsPckg = processingEnv.getOptions().get("package");
            final String pckg = ("ru.sbsoft." + (optionsPckg == null ? "common" : optionsPckg)).toLowerCase().replaceAll("-", ".");
            Set<String> templateTypes = agregator.getProcessorTypes();
            templateTypes = templateTypes != null ? new HashSet<>(templateTypes) : new HashSet<>();
            templateTypes.add(ru.sbsoft.shared.consts.SBFGridEnum.class.getName());
            final VelocityContext context = new VelocityContext();
            final String genClassName = "GridManager";
            context.put("genClassName", genClassName);
            context.put("templates", agregator.getProcessors());

            final Set<String> unitNames = agregator.getUnitNames();

            unitNames.addAll(dynagregator.getUnitNames());

            context.put("unitTemplates", unitNames);

            context.put("dyntemplates", dynagregator.getProcessors());
            context.put("templateTypes", templateTypes);
            context.put("dynTemplateTypes", dynagregator.getProcessorTypes());
            context.put("injections", injections);
            context.put("now", new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date()));
            context.put("package", pckg);
            context.put("globalQueryContext", gc);
            context.put("sqlBuilder", sb);
            context.put("templateBuilder", tb);
            if (appTemplateManager != null) {
                context.put("templateManager", appTemplateManager);
            }
            //
            final JavaFileObject outFile = processingEnv.getFiler().createSourceFile(pckg + "." + genClassName);
            VelocityUtils.createSingleFile(outFile, "gridManager.vm", context);
        } catch (Exception ex) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "GridManagerGenerator error " + ex.getMessage());
            ex.printStackTrace();
        }

        return false;
    }
}
