package ru.sbsoft.server.utils;

import java.util.Locale;
import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.Ii18nDao;
import ru.sbsoft.shared.api.i18n.LocalizedParamString;
import ru.sbsoft.shared.api.i18n.NonLocalizedParamString;
import ru.sbsoft.shared.exceptions.ApplicationException;

/**
 *
 * @author Kiselev
 */
public class AppException extends ApplicationException {

    private String msg;

    public AppException() {
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Object... args) {
        super(args != null && args.length > 0 ? String.format(message, args) : message);
    }

    public AppException(I18nResourceInfo resourceInfo, String... parameters) {
        super(resourceInfo, parameters);
    }

    public AppException(I18nResourceInfo resourceInfo) {
        super(resourceInfo);
    }

    @Override
    public String getMessage() {
        return msg != null ? msg : super.getMessage();
    }

    public AppException initMsg(Ii18nDao i18n) {
        if (i18n != null) {
            ILocalizedString str = getLocalizedString();
            ILocalizedString[] params = null;
            if (str instanceof LocalizedParamString) {
                params = ((LocalizedParamString) str).getParams();
            } else if (str instanceof NonLocalizedParamString) {
                params = ((NonLocalizedParamString) str).getParams();
            }
            final String lang = Locale.US.getLanguage();
            msg = params != null ? i18n.get(lang, str.getResourceInfo(), params) : i18n.get(lang, str.getResourceInfo());
        }
        return this;
    }

}
