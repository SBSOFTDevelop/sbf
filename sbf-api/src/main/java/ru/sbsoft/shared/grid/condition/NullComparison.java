package ru.sbsoft.shared.grid.condition;

import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.interfaces.NamedItem;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;

/**
 *
 * @author Kiselev
 */
public enum NullComparison implements NamedItem {

    NULL("is null"),
    NOT_NULL("is not null");

    private final ILocalizedString itemName;

    NullComparison(String itemName) {
        this.itemName = new NonLocalizedString(itemName);
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public ILocalizedString getItemName() {
        return itemName;
    }

}
