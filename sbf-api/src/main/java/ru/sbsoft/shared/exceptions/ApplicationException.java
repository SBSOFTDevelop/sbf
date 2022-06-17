package ru.sbsoft.shared.exceptions;

import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.IParamString;
import ru.sbsoft.shared.api.i18n.LocalizedParamString;
import ru.sbsoft.shared.api.i18n.LocalizedString;
import ru.sbsoft.shared.api.i18n.NonLocalizedParamString;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;

/**
 * @author balandin
 * @since Jul 5, 2013 12:09:56 PM
 */
public class ApplicationException extends RuntimeException {

    private ILocalizedString localizedString;

    private String msg;

    public ApplicationException() {
        super();
    }

    public ApplicationException(ILocalizedString message) {
        super();
        this.localizedString = message;
    }

    public ApplicationException(I18nResourceInfo resourceInfo, Throwable cause, ILocalizedString... parameters) {
        super(cause);
        if (null == parameters) {
            this.localizedString = new LocalizedString(resourceInfo);
        } else {
            this.localizedString = new LocalizedParamString(resourceInfo, parameters);
        }
    }

    public ApplicationException(I18nResourceInfo resourceInfo, Throwable cause, String... parameters) {
        super(cause);
        if (null == parameters) {
            this.localizedString = new LocalizedString(resourceInfo);
        } else {
            this.localizedString = new LocalizedParamString(resourceInfo, parameters);
        }
    }

    public ApplicationException(I18nResourceInfo resourceInfo, Throwable cause) {
        super(cause);
        this.localizedString = new LocalizedString(resourceInfo);
    }

    public ApplicationException(I18nResourceInfo resourceInfo, String... parameters) {
        super();
        if (null == parameters) {
            this.localizedString = new LocalizedString(resourceInfo);
        } else {
            this.localizedString = new LocalizedParamString(resourceInfo, parameters);
        }
    }

    public ApplicationException(I18nResourceInfo resourceInfo, ILocalizedString... parameters) {
        this(resourceInfo, null, parameters);
    }

    public ApplicationException(I18nResourceInfo resourceInfo) {
        super();
        this.localizedString = new LocalizedString(resourceInfo);
    }

    public ApplicationException(String message, Throwable cause, String... parameters) {
        super(message, cause);
        if (null == parameters) {
            this.localizedString = new NonLocalizedString(message);
        } else {
            this.localizedString = new NonLocalizedParamString(message, parameters);
        }
    }

    public ApplicationException(String message, String... parameters) {
        this(message, null, parameters);
    }

    public ApplicationException(String message) {
        this(message, null, (String[]) null);
    }

    public ILocalizedString getLocalizedString() {
        return localizedString;
    }

    @Override
    public String getMessage() {
        if (msg == null) {
            StringBuilder buf = new StringBuilder();
            if (super.getMessage() != null) {
                buf.append(super.getMessage());
            } else {
                addStr(buf, localizedString);
            }
            if (localizedString instanceof IParamString) {
                buf.append(" [");
                ILocalizedString[] params = ((IParamString) localizedString).getParams();
                if (params != null && params.length > 0) {
                    for (ILocalizedString p : params) {
                        addStr(buf, p);
                        buf.append("; ");
                    }
                    buf.setLength(buf.length() - 2);
                }
                buf.append(']');
            }
            msg = buf.toString();
        }
        return msg;
    }

    private static void addStr(StringBuilder buf, ILocalizedString s) {
        if (s.getDefaultName() != null) {
            buf.append(s.getDefaultName());
        } else if (s.getResourceInfo() != null) {
            buf.append("{lib: ").append(s.getResourceInfo().getLib().getName());
            buf.append(", key: ").append(s.getResourceInfo().getKey()).append('}');
        }
    }

}
