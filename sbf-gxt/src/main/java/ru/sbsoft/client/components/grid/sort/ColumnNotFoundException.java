package ru.sbsoft.client.components.grid.sort;

import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 *
 * @author Kiselev
 */
public class ColumnNotFoundException extends Exception {
    private final String alias;

    public ColumnNotFoundException(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    @Override
    public String getLocalizedMessage() {
        return I18n.get(SBFBrowserStr.msgMultiSortColumnNotFound, alias);
    }

    @Override
    public String getMessage() {
        return "Column '" + alias + "' is not is columns list";
    }
    
}
