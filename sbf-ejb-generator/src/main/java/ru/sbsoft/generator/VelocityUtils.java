package ru.sbsoft.generator;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.Properties;
import javax.tools.JavaFileObject;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class VelocityUtils {

    static {
        final Properties props = new Properties();
        props.put("resource.loader", "cls");
        props.put("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.JdkLogChute");
        props.put("cls.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        try {
            Velocity.init(props);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void createSingleFile(JavaFileObject outFile, String templateName, VelocityContext context) throws Exception {
        final Template template = Velocity.getTemplate(templateName, "utf-8");
        final OutputStreamWriter osw = new OutputStreamWriter(outFile.openOutputStream(), "utf-8");
        final BufferedWriter bw = new BufferedWriter(osw);
        template.merge(context, bw);
        bw.flush();
        bw.close();
    }
}
