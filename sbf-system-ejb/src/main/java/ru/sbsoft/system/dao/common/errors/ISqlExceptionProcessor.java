package ru.sbsoft.system.dao.common.errors;

import java.sql.SQLException;
import ru.sbsoft.shared.api.i18n.ILocalizedString;

/**
 * Преобразователь ошибки SQL в читаемый текст.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public interface ISqlExceptionProcessor {

    /**
     * Метод для обработки ошибки SQL.
     *
     * @param ex обрабатываемая ошибка.
     * @return читаемое сообщение об ошибке, если сообщение для соответствующего
     * кода найдено. В противном случае return null.
     */
    ILocalizedString getMessageForException(SQLException ex);
    
}
