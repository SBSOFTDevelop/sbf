package ru.sbsoft.client.utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.GXTLogConfiguration;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.info.DefaultInfoConfig;
import com.sencha.gxt.widget.core.client.info.Info;
import java.util.Collections;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.dialog.ErrorDialog;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;
import ru.sbsoft.shared.exceptions.ThrowableWrapper;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.exceptions.ApplicationException;

/**
 * Служебные функции для клиентской части приложения.
 *
 * @author panarin
 */
public class ClientUtils {

    private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ClientUtils.class.getName());

    private ClientUtils() {
    }

    /**
     * Универсальный обработчик ошибки. Получает управление до отображения
     * ошибки пользователю. Может отменить появление сообщения об ошибке.
     */
    public interface ErrorHandler {

        public boolean onError(Throwable throwable);

        public boolean onServerError(ThrowableWrapper throwableWrapper);
    }

    /**
     * Отображает сообщение об ошибке. Без обработчика.
     *
     * @param caught ошибка
     */
    public static void alertException(Throwable caught) {
        alertException(caught, null);
    }

    /**
     * Отображает сообщение об ошибке.
     *
     * @param caught ошибка
     * @param errorHandler обработчик ошибки. Если метод {@code onError}
     * возвратит true, сообщение не будет показано.
     */
    public static void alertException(Throwable caught, ErrorHandler errorHandler) {
        //if (!GWT.isProdMode()) {
        //    GWT.log("Erro!", caught);
        //}

        doLog("allert Exception");

        if (caught instanceof InvocationException) {
            if (caught.getMessage().contains("j_password")) {
                MessageBox box = new MessageBox(I18n.get(SBFGeneralStr.captWarning));
                box.setWidth(500);
                box.setIcon(MessageBox.ICONS.warning());
                box.setMessage(I18n.get(SBFGeneralStr.msgUserSessionCompleted));
                box.addHideHandler(new HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        reload();
                    }
                });
                box.show();
                return;
            }
        }

        boolean canceled = false;
        try {
            if (errorHandler != null) {
                canceled = errorHandler.onError(caught);

                doLog("allert Exception canceled=" + canceled);

            }
        } finally {
            if (canceled) {
                return;
            }
            showException(getExceptionMessage(caught), errorHandler);
        }
    }

    private static void doLog(String msg) {

        if (GXTLogConfiguration.loggingIsEnabled()) {
            logger.finest(msg);
        }

    }

    /**
     * Метод извлечения сообщения из исключения, для ApplicationException
     * добавлен функционал извлечения локализованного ресурса.
     *
     * @param ex исключение
     * @return сообщение для отображения
     */
    public static String getExceptionMessage(Throwable ex) {
        String message = ex.getLocalizedMessage();
        if (ex instanceof ApplicationException) {
            ApplicationException appException = (ApplicationException) ex;
            message = I18n.get(appException.getLocalizedString());
        }
        return message;
    }

    /**
     * Перезагрузка приложения в браузере.
     */
    public static void reload() {
        Window.Location.assign(Window.Location.getHref());
    }

    /**
     * Отображает информационное сообщение пользователю.
     *
     * @param heading заголовок окна
     * @param message сообщение
     */
    public static void message(final String heading, String message) {
        final MessageBox messageBox = new MessageBox(SafeHtmlUtils.fromString(heading), SafeHtmlUtils.fromTrustedString(message));
        messageBox.setIcon(MessageBox.ICONS.info());
        messageBox.show();  
    }

    /**
     * Отображает предупреждающее сообщение пользователю.
     *
     * @param textInfo сообщение
     */
    public static void alertWarning(String textInfo) {
        final MessageBox messageBox = new MessageBox(I18n.get(SBFGeneralStr.captWarning), textInfo);
        messageBox.setIcon(MessageBox.ICONS.warning());
        messageBox.show();
    }

    /**
     * Отображает предупреждающее сообщение пользователю.
     *
     * @param caption заголовок окна
     * @param textInfo сообщение
     */
    public static void alertWarning(String caption, String textInfo) {
        final MessageBox messageBox = new MessageBox(caption, textInfo);
        messageBox.setIcon(MessageBox.ICONS.warning());
        messageBox.show();
    }

    public static void alertUnderConstruction(String textInfo) {
        final MessageBox messageBox = new MessageBox(I18n.get(SBFGeneralStr.msgInProcessDeveloping), textInfo);
        messageBox.setIcon(MessageBox.ICONS.warning());
        messageBox.show();
    }

    public static String convertMessage(ThrowableWrapper t) {
        String message = I18n.get(t.getLocalizedString());
        if (null == message) {
            message = t.getDetailMessage();
        }
        return message;
    }

//    public static void showError(final Object sender, final Throwable caught, final String message) {
//        GWT.log(sender.getClass().getName(), caught);
//        showException(message);
//    }
    private static void showException(final String message, final ErrorHandler errorHandler) {
        SBFConst.SERVICE_SERVICE.getLastError(new AsyncCallback<ThrowableWrapper>() {
            @Override
            public void onFailure(Throwable caught) {
                defaultMessage();
            }

            @Override
            public void onSuccess(ThrowableWrapper result) {
                boolean processed = handleException(result);
                if (processed) {
                    return;
                }

                boolean canceled = false;
                try {
                    if (errorHandler != null) {
                        canceled = errorHandler.onServerError(result);
                    }
                } finally {
                    if (!canceled) {
                        if (result == null) {
                            defaultMessage();
                        } else {
                            ErrorDialog dialog = new ErrorDialog();
                            dialog.show(result);
                        }
                    }
                }
            }

            private void defaultMessage() {
                showError(message);
            }

            private boolean handleException(ThrowableWrapper result) {
                if (result != null && result.getCause() == null && result.getClassName() == null) {
                    showError(convertMessage(result));
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Отображает пользователю сообщение об ошибке.
     *
     * @param message сообщение
     */
    public static void showError(String message) {
        if (message == null) {
            message = "empty message";
        }
        final AlertMessageBox alertBox = new AlertMessageBox(SafeHtmlUtils.fromString(I18n.get(SBFGeneralStr.captError)), SafeHtmlUtils.fromTrustedString(message.replace("\n", "<br/>")));
        alertBox.show();
    }

    /**
     * Отображает пользователю уведомление в правом нижнем углу.
     *
     * @param title заголовок
     * @param message сообщение
     */
    public static void display(String title, String message) {
        DefaultInfoConfig c = new DefaultInfoConfig(title, message);
        c.setWidth(500);
        c.setDisplay(2000);
        Info.display(c);
    }

    /**
     * Возвращает пустой список, если переданный объект NULL.
     *
     * @param <T> любой тип
     * @param v - список
     * @return переданный объект или пустой список, если переданный оъект равен
     * NULL.
     */
    public static <T> List<T> coalesce(List<T> v) {
        return v != null ? v : Collections.EMPTY_LIST;
    }

    /**
     * Сравнивает спики поэлементно.
     *
     * @param list1 список1
     * @param list2 список2
     * @return true если списки равны с содержимым
     */
    public static boolean equals(List list1, List list2) {
        if (list1 == null) {
            return list2 == null || list2.isEmpty();
        }
        if (list2 == null) {
            return list1.isEmpty();
        }
        if (list1.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list1.size(); i++) {
            Object object1 = list1.get(i);
            Object object2 = list2.get(i);
            boolean result = true;
            if (object1 != null) {
                result = object1.equals(object2);
            } else if (object2 != null) {
                result = object2.equals(object1);
            }
            if (!result) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return информация об интернет-браузере
     */
    public static native String getUserAgent() /*-{
     return navigator.userAgent.toLowerCase();
     }-*/;
    public static String[] EMPTY_STR_ARRAY = new String[]{};

    /**
     * @param <T> тип объектов
     * @param values список объектов
     * @return возвращает первый не null объект
     */
    public static <T> T coalesce(T... values) {
        for (T v : values) {
            if (v != null) {
                return v;
            }
        }
        return null;
    }

    public static boolean equals(Object v1, Object v2) {
        if (v1 != null) {
            return v1.equals(v2);
        }
        if (v2 != null) {
            return v2.equals(v1);
        }
        return true;
    }

    public static boolean equals(String[] a1, String[] a2) {
        a1 = (String[]) coalesce(a1, EMPTY_STR_ARRAY);
        a2 = (String[]) coalesce(a2, EMPTY_STR_ARRAY);
        if (a1.length != a2.length) {
            return false;
        }
        for (int i = 0; i < a2.length; i++) {
            if (!Strings.equals(a1[i], a2[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return URL приложения в браузере.
     */
    public static String getAppURL() {
        int lastSlash = GWT.getModuleBaseURL().substring(0, GWT.getModuleBaseURL().length() - 1).lastIndexOf('/');
        return GWT.getModuleBaseURL().substring(0, lastSlash + 1);
    }

    public static void center(Component c, Element parent) {
        Point p = c.getElement().getAlignToXY(parent, new AnchorAlignment(Anchor.CENTER, Anchor.CENTER), 0, 0);
        c.setPagePosition(p.getX(), p.getY());
    }

    public static void confirm(Component parent, I18nResourceInfo msg, final Command command) {
        confirm(parent, I18n.get(msg), command);
    }

    public static void confirm(Component parent, String msg, final Command command) {
        final ConfirmMessageBox box = new ConfirmMessageBox(I18n.get(SBFBrowserStr.labelConfirmation), msg);
        box.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
        box.addDialogHideHandler(new DialogHideEvent.DialogHideHandler() {
            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == PredefinedButton.OK) {
                    command.execute();
                }
            }
        });
        box.show();
        if (parent != null) {
            center(box, parent.getElement());
        }
    }

    public static com.sencha.gxt.widget.core.client.Window findWindow(Widget c) {
        Widget w = c;
        while (w != null && !(w instanceof com.sencha.gxt.widget.core.client.Window)) {
            w = w.getParent();
        }
        return (com.sencha.gxt.widget.core.client.Window) w;
    }

    public static boolean isAssignableFrom(Class c1, Class c2) {
        if (c1 == null || c2 == null) {
            return false;
        }
        Class c = c2;
        while (c != null) {
            if (c1.equals(c)) {
                return true;
            }
            c = c.getSuperclass();
        }
        return false;
    }
    public static final String EMPTY = "";
    private static final int PAD_LIMIT = 8192;

    public static String repeat(String str, int repeat) {
        // Performance tuned for 2.0 (JDK1.4)

        if (str == null) {
            return null;
        }
        if (repeat <= 0) {
            return EMPTY;
        }
        int inputLength = str.length();
        if (repeat == 1 || inputLength == 0) {
            return str;
        }
        if (inputLength == 1 && repeat <= PAD_LIMIT) {
            return padding(repeat, str.charAt(0));
        }

        int outputLength = inputLength * repeat;
        switch (inputLength) {
            case 1:
                char ch = str.charAt(0);
                char[] output1 = new char[outputLength];
                for (int i = repeat - 1; i >= 0; i--) {
                    output1[i] = ch;
                }
                return new String(output1);
            case 2:
                char ch0 = str.charAt(0);
                char ch1 = str.charAt(1);
                char[] output2 = new char[outputLength];
                for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
                    output2[i] = ch0;
                    output2[i + 1] = ch1;
                }
                return new String(output2);
            default:
                StringBuffer buf = new StringBuffer(outputLength);
                for (int i = 0; i < repeat; i++) {
                    buf.append(str);
                }
                return buf.toString();
        }
    }

    private static String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
        if (repeat < 0) {
            throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
        }
        final char[] buf = new char[repeat];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = padChar;
        }
        return new String(buf);
    }
}
