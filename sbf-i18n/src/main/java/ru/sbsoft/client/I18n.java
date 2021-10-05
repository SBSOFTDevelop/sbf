package ru.sbsoft.client;

import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.client.i18n.GwTi18nUnit;
import ru.sbsoft.shared.api.i18n.ILocalizedString;

/**
 *
 * @author Kiselev
 */
public final class I18n {

    public static String get(I18nResourceInfo inf, ILocalizedString... parameters) {
        return GwTi18nUnit.getInstance().get(inf, parameters);
    }

    public static String get(I18nResourceInfo inf, String... parameters) {
        return GwTi18nUnit.getInstance().get(inf, parameters);
    }

    public static String get(I18nResourceInfo inf) {
        return GwTi18nUnit.getInstance().get(inf);
    }

    public static String get(ILocalizedString object) {
        return GwTi18nUnit.getInstance().i18n(object);
    }

    private I18n() {
    }
}
