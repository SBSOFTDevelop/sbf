package ru.sbsoft.system.dao.common.errors;

import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.LocalizedString;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;

/**
 *
 * @author Sokoloff
 */
public enum DBError {
    ERR_CODE_IS_USE(SBFExceptionStr.codeAlreadyUse),
    ERR_FOUND_DEPENDING(SBFExceptionStr.cannotDeleteRecord);
    private ILocalizedString errMsg = null;

    DBError(I18nResourceInfo resourceInfo) {
        this.errMsg = new LocalizedString(resourceInfo);
    }

/*    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;

    }
*/
    public ILocalizedString getErrMsg() {
        return errMsg;
    }

}
