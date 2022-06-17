package ru.sbsoft.client.schedule.i18n;

import com.google.gwt.user.client.Cookies;
import java.util.List;
import java.util.Map;

/**
 * Компонент определяет установленную на клиентской части локаль. Локаль определяется параметором в URL: ?locale=ru
 * @author Sokoloff
 */
public class SBFi18nLocale {

    private SBFi18nLocale() {
    }

    private static final String SBLIB_LOCALE = "locale";

    public static String getLocaleName() {
        final Map<String, List<String>> parameters = com.google.gwt.user.client.Window.Location.getParameterMap();
        if (parameters.containsKey(SBLIB_LOCALE)) {
            return parameters.get(SBLIB_LOCALE).get(0);
        }
        String locale = getCurrentLocale();
        if (null != locale) {
            return locale;
        }
        if (Cookies.getCookieNames().contains(SBLIB_LOCALE)) {
            return Cookies.getCookie(SBLIB_LOCALE);
        }
        return null;
    }
    
    private static String getCurrentLocale() {
        String locale = getNativeLocale();
        if (null == locale) {
            return null;
        }
        return locale.replaceAll("-", "_");
    }
    
    private static native String getNativeLocale() /*-{
      var language = window.navigator.language;
      if (language == null) {
            language = window.navigator.userLanguage;
      }
      return language;      
    }-*/; 

}
