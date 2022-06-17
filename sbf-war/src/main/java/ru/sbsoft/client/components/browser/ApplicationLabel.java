package ru.sbsoft.client.components.browser;

import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 * Метки полей информации о приложении
 * @author Sokoloff
 */
public class ApplicationLabel {

    public String getVersion() {
        return I18n.get(SBFGeneralStr.labelVersion);
    }

    public String getRevision() {
        return I18n.get(SBFGeneralStr.labelRevision);
    }

    public String getTimestamp() {
        return I18n.get(SBFGeneralStr.labelTimestamp);
    }

    public String getServer() {
        return I18n.get(SBFGeneralStr.labelServer);
    }

}
