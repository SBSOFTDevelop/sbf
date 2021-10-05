package ru.sbsoft.system.dao.common.errors;

import ru.sbsoft.model.SQLRetCode;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 * Стандартный обработчик, основанный на кодах ошибок.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class NameCodeExceptionProcessor extends CodeBasedExceptionProcessor {

    private final Map<String, ILocalizedString> messages = new HashMap<>();
    private final Map<String, Pattern> patterns = new HashMap<>();

    public NameCodeExceptionProcessor() {
    }

    public void addMessageForSubstr(String code, String nonLocalizedMsg) {
        addMessageForSubstr(code, new NonLocalizedString(nonLocalizedMsg));
    }

    public void addMessageForSubstr(NamedItem message) {
        addMessageForSubstr(message.getCode(), message.getItemName());
    }

    public void addMessageForSubstr(String code, ILocalizedString msg) {
        String key = code.toLowerCase();
        messages.put(key, msg);
        patterns.put(key, Pattern.compile("\\b" + key + "\\b", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));
    }

    public ILocalizedString getMessageForSqlMsg(String sqlMsg) {
        if (!(sqlMsg == null || sqlMsg.isEmpty())) {
            for (Map.Entry<String, ILocalizedString> e : messages.entrySet()) {
                if (patterns.get(e.getKey()).matcher(sqlMsg).find()) {
                    return e.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public ILocalizedString getMessage(SQLRetCode code, String message) {
        final ILocalizedString msg = getMessageForSqlMsg(message);
        return (null == msg) ? super.getMessage(code, message) : msg;

    }

    @Override
    public ILocalizedString getMessageForException(SQLException ex) {
        ILocalizedString msg = getMessageForSqlMsg(ex.getMessage());
        return msg != null ? msg : getMessage(new SQLRetCode(ex.getSQLState(), ex.getErrorCode()), ex.getMessage());
    }
}
