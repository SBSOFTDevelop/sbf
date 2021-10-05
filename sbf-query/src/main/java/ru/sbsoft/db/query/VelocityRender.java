package ru.sbsoft.db.query;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapterImpl;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.log.NullLogChute;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author balandin
 * @since Apr 30, 2014 3:12:44 PM
 */
public class VelocityRender {

    private final static Logger LOGGER = LoggerFactory.getLogger(VelocityRender.class);
    private final static RuntimeInstance INSTANCE;

    static {
        INSTANCE = new RuntimeInstance();
        try {
            Properties properties = new Properties();
            properties.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS, NullLogChute.class.getName());
            INSTANCE.init(properties);
        } catch (Exception ex) {
            throw new RuntimeException("Initialization error Velocity RuntimeInstance.", ex);
        }
    }

    public static String parse(final Context context, final String src) throws Exception {
        try {
            final SimpleNode nodeTree = INSTANCE.parse(new StringReader(src), src);
            final StringWriter out = new StringWriter(src.length());
            final InternalContextAdapterImpl ica = new InternalContextAdapterImpl(context);
            ica.pushCurrentTemplateName(src);
            try {
                nodeTree.init(ica, INSTANCE);
                nodeTree.render(ica, out);
                return out.toString();
            } finally {
                ica.popCurrentTemplateName();
            }
        } catch (ParseException e) {
            LOGGER.debug(src);
            throw e;
        }
    }
}
