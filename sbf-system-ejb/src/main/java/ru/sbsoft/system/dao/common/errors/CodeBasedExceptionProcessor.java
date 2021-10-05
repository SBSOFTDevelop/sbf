package ru.sbsoft.system.dao.common.errors;

import ru.sbsoft.model.SQLRetCode;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import ru.sbsoft.shared.api.i18n.ILocalizedString;

/**
 * Стандартный обработчик, основанный на кодах ошибок.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class CodeBasedExceptionProcessor implements ICodeBasedExceptionProcessor {

    private final Map<SQLRetCode, ILocalizedString> messages = new HashMap<>();

    public CodeBasedExceptionProcessor() {

    }

    @Override
    public void addMessageForCode(SQLRetCode code, ILocalizedString message) {
        messages.put(code, message);
    }

    @Override
    public ILocalizedString getMessage(SQLRetCode code, String message) {
        return messages.get(code);
    }

    @Override
    public ILocalizedString getMessageForException(SQLException ex) {
        return getMessage(new SQLRetCode(ex.getSQLState(), ex.getErrorCode()), ex.getMessage());
    }
}
