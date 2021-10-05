package ru.sbsoft.shared.interfaces;

/**
 * Идентификатор таблицы браузера Может служить для проверки прав (только
 * чтение, добавление, редактирование записей), но не рекомендуется
 *
 * @author balandin
 * @since May 24, 2013 3:26:29 PM
 */
public interface GridType extends ObjectType {

    String PARENT_ID_PARAM_NAME_POSTFIX = "_PARENT_ID";

    default String getParentIdName() {
        final String code;
        if (this instanceof Enum) {
            code = ((Enum) this).name();
        } else {
            code = getCode();
        }
        if (code == null) {
            return PARENT_ID_PARAM_NAME_POSTFIX;
        } else {
            return new StringBuilder(code.length() + PARENT_ID_PARAM_NAME_POSTFIX.length()).append(code).append(PARENT_ID_PARAM_NAME_POSTFIX).toString();
        }
    }
}
