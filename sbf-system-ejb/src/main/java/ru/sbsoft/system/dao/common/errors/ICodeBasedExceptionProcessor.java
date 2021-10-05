package ru.sbsoft.system.dao.common.errors;

import ru.sbsoft.model.SQLRetCode;
import ru.sbsoft.shared.api.i18n.ILocalizedString;



/**
 *
 * @author Kiselev
 */
public interface ICodeBasedExceptionProcessor extends ISqlExceptionProcessor {

    void addMessageForCode(SQLRetCode code, ILocalizedString message);

    ILocalizedString getMessage(SQLRetCode code, String message);
    
}
