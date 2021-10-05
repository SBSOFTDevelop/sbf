package ru.sbsoft.server.services;

import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import ru.sbsoft.shared.api.i18n.Ii18nDao;
import ru.sbsoft.shared.services.Ii18nService;
import ru.sbsoft.shared.services.ServiceConst;
import ru.sbsoft.shared.api.i18n.i18nModuleInfo;
import ru.sbsoft.shared.consts.I18nSection;

/**
 * Реализация сервиса интернационализации.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
@WebServlet(urlPatterns = {ServiceConst.I18N_SERVICE_LONG})
public class i18nService extends SBFRemoteServiceServlet implements Ii18nService {

    @EJB
    private Ii18nDao i18unit;

    private static final Map<String, i18nModuleInfo> cache = new HashMap<>();

    @Override
    public i18nModuleInfo geTi18n(String locale, I18nSection[] files) {
        if (isInCache(locale)) {
            return cache.get(locale);
        }

        return putInCache(locale, i18unit.geTi18n(locale, files));
    }

    private boolean isInCache(String locale) {
        return cache.containsKey(locale);
    }

    private i18nModuleInfo putInCache(String locale, i18nModuleInfo result) {
        cache.put(locale, result);
        return result;
    }

}
