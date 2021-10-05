package ru.sbsoft.system.dao.common;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Класс переопределяет механизм загрузки локализованных строковых ресурсов: если не найден ресурс с указанной локалью,
 * то по умолчанию сразу загружается ресурс без локали, а не ресурс с локалью сервера.
 *
 * @author Sokoloff
 */
public class SBFResourseControl extends ResourceBundle.Control {

    private static SBFResourseControl instance;

    private SBFResourseControl() {
        super();
    }

    public static SBFResourseControl getInstance() {
        if (null == instance) {
            instance = new SBFResourseControl();
        }
        return instance;
    }

    @Override
    public Locale getFallbackLocale(String baseName, Locale locale) {
        if (baseName == null) {
            throw new NullPointerException();
        }
        return null;
    }

}
