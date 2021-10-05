package ru.sbsoft.client.components.actions;

import com.sencha.gxt.widget.core.client.Component;

/**
 * Вспомогательные функции для работы с действиями пунктов меню форм и
 * браузеров.
 *
 * @author balandin
 * @since Oct 7, 2013 2:48:15 PM
 */
public class ActionUtils {

/**
 * @param source
 * @return прикрепленный к пункту меню объект действия.
 */
    public static Action findAction(final Object source) {
        if (source instanceof Component) {
            final Object data = ((Component) source).getData(Action.LABEL);
            if (data instanceof Action) {
                return ((Action) data);
            }
        }
        return null;
    }
}
