package ru.sbsoft.dao.errors;

import ru.sbsoft.model.SQLRetCode;
import ru.sbsoft.shared.api.i18n.ILocalizedString;

/**
 * Интерфейс представляет метод для обработки ошибок SQL
 *
 * @author Kiselev
 */
public interface ISqlErrors {

    /**
     ** Метод для обработки ошибки SQL.
     *
     * @param sqlException обрабатываемая ошибка (экземпляр исключения).
     * @return читаемое сообщение об ошибке, если сообщение для соответствующего
     * кода найдено. В противном случае return null.
     */
    //ILocalizedString getMessageForException(SQLException sqlException);

    ILocalizedString getMessage(SQLRetCode rc, String message);

    String getCurrentDatabase();
}
