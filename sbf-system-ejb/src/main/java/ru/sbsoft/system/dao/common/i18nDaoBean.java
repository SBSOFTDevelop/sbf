package ru.sbsoft.system.dao.common;

import ru.sbsoft.shared.api.i18n.i18nLibrary;
import ru.sbsoft.shared.api.i18n.i18nModuleInfo;
import ru.sbsoft.shared.api.i18n.i18nMessage;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.security.PermitAll;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.Ii18nDao;
import ru.sbsoft.shared.api.i18n.i18nUtils;
import ru.sbsoft.shared.consts.I18nSection;
import ru.sbsoft.shared.api.i18n.IParamString;

@Remote(Ii18nDao.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@PermitAll
public class i18nDaoBean implements Ii18nDao {

    private final static String RESOURCE_PATH = "ru.sbsoft.sbf.i18n";

    private static final Logger LOGGER = LoggerFactory.getLogger(i18nDaoBean.class);
    private final Map<String, Map<String, Map<String, i18nMessage>>> cacheLocale = new HashMap<>();

    @Override
    public i18nModuleInfo geTi18n(String locale, I18nSection[] files) {
        final i18nModuleInfo info = new i18nModuleInfo();

        for (I18nSection sec : files) {
            String library = sec.getName();
            final i18nLibrary libraryInfo = new i18nLibrary();
            libraryInfo.setLibraryName(library.substring(library.lastIndexOf(".") + 1));
            info.getLibraries().add(libraryInfo);

            final ResourceBundle bundle = getBundle(library, locale);
            final Enumeration<String> keys = bundle.getKeys();

            while (keys.hasMoreElements()) {
                final String key = keys.nextElement();
                final String value = bundle.getString(key);
                i18nMessage messages = createi18nMessage(key, value);
                libraryInfo.getMessages().add(messages);
            }
        }
        return info;
    }

    @Override
    public String i18n(String i18nString) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String get(String locale, I18nSection lib, String key, ILocalizedString... parameters) {
        String library = lib != null ? lib.getName() : null;
        if (!isExistsLibrary(locale, library)) {
            loadResource(locale, library);
        }
        if (cacheLocale.containsKey(locale) && cacheLocale.get(locale).containsKey(library) && cacheLocale.get(locale).get(library).containsKey(key)) {
            return i18nUtils.fillParameters(cacheLocale.get(locale).get(library).get(key).getValue(), convertParameters(locale, parameters));
        } else {
            LOGGER.error(String.format("Key %s in library %s not found.", key, library));
            return key;
        }
    }

    @Override
    public String get(String locale, ILocalizedString item) {
        if (null == item) {
            return null;
        }

        if (null == item.getResourceInfo()) {

            String[] stringParameters = null;
            if (item instanceof IParamString) {
                stringParameters = convertParameters(locale, ((IParamString) item).getParams());
            }
            return i18nUtils.fillParameters(item.getDefaultName(), stringParameters);

        } else {

            ILocalizedString[] localParameters = null;
            if (item instanceof IParamString) {
                localParameters = ((IParamString) item).getParams();
            }
            return get(locale, item.getResourceInfo(), localParameters);
        }

    }

    @Override
    public String get(String locale, I18nResourceInfo resourceInfo, ILocalizedString... parameters
    ) {
        return get(locale, resourceInfo.getLib(), resourceInfo.getKey(), parameters);
    }

    @Override
    public String get(String locale, I18nResourceInfo resourceInfo, String... parameters) {
        return get(locale, resourceInfo, i18nUtils.convertStringParams(parameters));
    }

    @Override
    public String get(String locale, I18nResourceInfo resourceInfo) {
        return get(locale, resourceInfo, (ILocalizedString) null);
    }

    private ResourceBundle getBundle(String library, String locale) {
        return ResourceBundle.getBundle(RESOURCE_PATH + "." + library, locale == null ? Locale.getDefault() : new Locale(locale), SBFResourseControl.getInstance());
    }

    private boolean isExistsLibrary(String locale, String library) {
        if (!cacheLocale.containsKey(locale)) {
            return false;
        }
        return cacheLocale.get(locale).containsKey(library);
    }

    private void loadResource(String locale, String library) {
        final Map<String, Map<String, i18nMessage>> cacheLibrary = new HashMap<>();
        cacheLocale.put(locale, cacheLibrary);
        final Map<String, i18nMessage> cacheKeys = new HashMap<>();
        cacheLibrary.put(library, cacheKeys);
        final ResourceBundle bundle = getBundle(library, locale);
        final Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            final String key = keys.nextElement();
            final String value = bundle.getString(key);
            i18nMessage messages = createi18nMessage(key, value);
            cacheKeys.put(key, messages);
        }
    }

    private i18nMessage createi18nMessage(String key, String value) {
        final i18nMessage message = new i18nMessage(key, value);
        message.setParamCount(value.replaceAll("[^{]", "").length());
        return message;
    }

    private String[] convertParameters(String locale, ILocalizedString... parameters) {
        if (null == parameters) {
            return null;
        }
        String[] stringArray = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            stringArray[i] = get(locale, parameters[i]);
        }
        return stringArray;
    }
}
