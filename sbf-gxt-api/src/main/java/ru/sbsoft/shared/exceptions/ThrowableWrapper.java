package ru.sbsoft.shared.exceptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.api.i18n.ILocalizedString;

/**
 * @author balandin
 * @since May 16, 2013 4:53:25 PM
 */
public class ThrowableWrapper implements Serializable {

    private String className;
    private String detailMessage;
    private ILocalizedString localizedString;
    private StackTraceElementModel[] stackTrace;
    private ThrowableWrapper cause;

    public ThrowableWrapper() {
    }

    public ThrowableWrapper(Throwable throwable) {
        super();

        ApplicationException applicatonException = findApplicatonException(throwable);
        if (applicatonException != null) {
            this.detailMessage = applicatonException.getMessage();
            this.localizedString = applicatonException.getLocalizedString();
        } else {
            this.className = throwable.getClass().getName();
            this.detailMessage = throwable.getMessage();
            this.stackTrace = makeStackTraceModelList(throwable.getStackTrace());
            this.cause = makeCauseWrapper(throwable.getCause());
        }
    }

    private static ApplicationException findApplicatonException(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        if (throwable instanceof ApplicationException) {
            return (ApplicationException) throwable;
        }
        return findApplicatonException(throwable.getCause());
    }

    private static StackTraceElementModel[] makeStackTraceModelList(StackTraceElement[] stackTrace) {
        StackTraceElementModel[] result = new StackTraceElementModel[stackTrace.length];
        for (int i = 0; i < stackTrace.length; i++) {
            result[i] = new StackTraceElementModel(stackTrace[i]);
        }
        return result;
    }

    private static ThrowableWrapper makeCauseWrapper(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        return new ThrowableWrapper(throwable);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    public StackTraceElementModel[] getStackTrace() {
        return stackTrace;
    }

    public ILocalizedString getLocalizedString() {
        return localizedString;
    }

    public void setStackTrace(StackTraceElementModel[] stackTrace) {
        this.stackTrace = stackTrace;
    }

    public ThrowableWrapper getCause() {
        return cause;
    }

    public void setCause(ThrowableWrapper cause) {
        this.cause = cause;
    }

    @Override
    public String toString() {
        return Strings.join(new Object[]{className, detailMessage}, "", ": ", true);
    }

    public String generateTrace() {
        ArrayList<ThrowableWrapper> tmp = new ArrayList<ThrowableWrapper>();

        ThrowableWrapper t = this;
        while (t != null) {
            tmp.add(t);
            t = t.getCause();
        }

        Collections.reverse(tmp);

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < tmp.size(); i++) {
            t = tmp.get(i);
            s.append(color(t.toString(), "f00")).append("<br>");
            printStackTrace(s, t, (i == 0));
            s.append("<br>");
        }

        return s.toString();
    }

    private static void printStackTrace(StringBuilder s, ThrowableWrapper throwableWrapper, boolean isFirst) {
        if (null == throwableWrapper || null == throwableWrapper.getStackTrace()) {
            return;
        }
        int size = throwableWrapper.getStackTrace().length;
        size = isFirst ? size : Math.min(size, 3);
        for (int i = 0; i < size; i++) {
            s.append(Strings.repl("&nbsp;", 8)).append(throwableWrapper.getStackTrace()[i].toString()).append("<br>");
        }
        if (!isFirst) {
            s.append(Strings.repl("&nbsp;", 8)).append("...").append("<br>");
        }
    }

    private static String color(String value, String color) {
        return "<span style=\"color:#" + color + "\">" + value + "</span>";
    }

}
