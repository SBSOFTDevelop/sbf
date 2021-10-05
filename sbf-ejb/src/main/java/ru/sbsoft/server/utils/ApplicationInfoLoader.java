package ru.sbsoft.server.utils;

import java.io.InputStream;
import java.net.UnknownHostException;
import ru.sbsoft.common.BuildInfo;
import ru.sbsoft.common.Config;
import ru.sbsoft.common.ConfigException;
import ru.sbsoft.shared.ApplicationInfo;

/**
 * Заполняет модель <i>ApplicationInfo</i> 
 * значениями свойств <br> 
 * <ul>
 * <li>project.version
 * <li>project.version.time
 * <li>project.version.revision
 * </ul>
 *из ресурса <b>build.properties</b>
 * и именем хоста (сервер Приложений) из ресурса <b>com.sun.aas.instanceName</b>
 * @author balandin
 * @since Jul 3, 2014 1:31:13 PM
 */
public class ApplicationInfoLoader {

    public static ApplicationInfo load(Class resourceClass) {
        final InputStream resourceStream = resourceClass.getResourceAsStream("build.properties");
        Config config = null;
        if (resourceStream != null) {
            try {
                config = Config.load(resourceStream);
            } catch (ConfigException ignore) {
            }
        }

        ApplicationInfo result = new ApplicationInfo();

        BuildInfo info = new BuildInfo(config);
        result.setVersion(info.getVersion());
        result.setRevision(info.getRevision());
        result.setTimestamp(info.getBuidDateTime());
        String inst = System.getProperty("com.sun.aas.instanceName");
        String addr;
        try {
            addr = java.net.InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            addr = ex.getMessage();
        }
        result.setDescription((inst == null) ? addr : inst + " (" + addr + ")");
        return result;
    }
}
