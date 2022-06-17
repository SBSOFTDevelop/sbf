package ru.sbsoft.shared.interfaces;

import ru.sbsoft.shared.api.i18n.ILocalizedString;

/**
 * Маркер-идентификатор операции.
 *
 * @author balandin
 * @since May 24, 2013 2:57:33 PM
 */
public interface OperationType extends ObjectType {

    /**
     * @return Название операции для отображения в интерфейсе с локалью
     * пользователя
     */
    ILocalizedString getTitle();

    /**
     * @return Код права запуска операции
     */
    String getSecurityRole();

    default boolean isJms() {
        return false;
    }
}
