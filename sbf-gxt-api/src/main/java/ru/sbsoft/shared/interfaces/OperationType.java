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
    public ILocalizedString getTitle();

    /**
     * @return Код права запуска операции
     */
    public String getSecurityRole();

    public default boolean isJms() {
        return false;
    }
}
