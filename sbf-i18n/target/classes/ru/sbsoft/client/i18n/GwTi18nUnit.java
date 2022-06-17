package ru.sbsoft.client.i18n;

import ru.sbsoft.shared.api.i18n.i18nUnit;
import ru.sbsoft.shared.api.i18n.i18nModuleInfo;
import ru.sbsoft.shared.api.i18n.i18nLibrary;
import ru.sbsoft.shared.api.i18n.i18nMessage;
import com.google.gwt.core.client.GWT;
import java.util.HashMap;
import java.util.Map;
import ru.sbsoft.shared.api.i18n.I18nResource;
import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import static ru.sbsoft.shared.api.i18n.Ii18nDao.*;
import ru.sbsoft.shared.api.i18n.i18nUtils;
import ru.sbsoft.shared.consts.I18nSection;
import ru.sbsoft.shared.api.i18n.IParamString;

/**
 * Реализация клиентской интернационализации на GWT.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class GwTi18nUnit implements i18nUnit, I18nResource {

    private static final GwTi18nUnit INSTANCE = new GwTi18nUnit();
    private static final I18nSection DEFAULT_LIBRARY = new SimpleI18nSec("sbf_common");
    private final Map<String, Map<String, i18nMessage>> libraries = new HashMap<String, Map<String, i18nMessage>>();

    private GwTi18nUnit() {
    }

    public static GwTi18nUnit getInstance() {
        return INSTANCE;
    }

    public void addModuleInfo(i18nModuleInfo moduleInfo) {
        if (moduleInfo != null) {
            for (i18nLibrary lib : moduleInfo.getLibraries()) {
                final HashMap<String, i18nMessage> messages = new HashMap<String, i18nMessage>();
                libraries.put(lib.getLibraryName(), messages);
                for (i18nMessage message : lib.getMessages()) {
                    messages.put(message.getKey(), message);
                }
            }
        }
    }

    @Override
    public String i18n(String i18nString) {
        if (i18nString == null || !i18nString.startsWith(I18N_PROTOCOL)) {
            return i18nString;
        }

        final String key = i18nString.replaceAll(I18N_VALUE_REGEXP, "$1");
        final String library = i18nString.contains("library=") ? i18nString.replaceAll(I18N_LIBRARY_REGEXP, "$1") : null;

        return getFromLibrary(library != null ? new SimpleI18nSec(library) : null, key);
    }

    @Override
    public String i18n(I18nSection library, String key) {
        return getFromLibrary(library, key);
    }

    @Override
    public String i18n(ILocalizedString item) {
        if (null == item) {
            return null;
        }
        if (null == item.getResourceInfo()) {
            String[] stringParameters = null;
            if (item instanceof IParamString) {
                stringParameters = convertParameters(((IParamString) item).getParams());
            }
            return i18nUtils.fillParameters(item.getDefaultName(), stringParameters);
        } else {
            ILocalizedString[] localParameters = null;
            if (item instanceof IParamString) {
                localParameters = ((IParamString) item).getParams();
            }
            return get(item.getResourceInfo(), localParameters);
        }
    }

    @Override
    public String get(I18nResourceInfo inf, ILocalizedString... parameters) {
        return getFromLibrary(inf.getLib(), inf.getKey(), parameters);
    }

    @Override
    public String get(I18nResourceInfo inf, String... parameters) {
        return getFromLibrary(inf.getLib(), inf.getKey(), i18nUtils.convertStringParams(parameters));
    }

    @Override
    public String get(I18nResourceInfo inf) {
        String library = inf.getLib().getName();
        if (libraries.containsKey(library) && libraries.get(library).containsKey(inf.getKey())) {
            return libraries.get(library).get(inf.getKey()).getValue();
        } else {
            GWT.log("localisation string for " + inf.getKey() + " not found in library " + library);
            return inf.getKey();
        }
    }

    private String getFromLibrary(I18nSection lib, String key, ILocalizedString... parameters) {
        if (lib == null) {
            lib = DEFAULT_LIBRARY;
        }
        String library = lib.getName();
        if (libraries.containsKey(library) && libraries.get(library).containsKey(key)) {
            return i18nUtils.fillParameters(libraries.get(library).get(key).getValue(), convertParameters(parameters));
        } else {
            GWT.log("localisation string for " + key + " not found in library " + library);
            return key;
        }
    }

    private String[] convertParameters(ILocalizedString... parameters) {
        if (null == parameters) {
            return null;
        }
        String[] stringArray = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            stringArray[i] = i18n(parameters[i]);
        }
        return stringArray;
    }

    private static class SimpleI18nSec implements I18nSection {

        private String name;

        private SimpleI18nSec() {
        }

        public SimpleI18nSec(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

    }

}
