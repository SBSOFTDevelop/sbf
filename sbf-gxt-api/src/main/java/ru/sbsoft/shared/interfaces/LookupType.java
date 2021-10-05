package ru.sbsoft.shared.interfaces;

import ru.sbsoft.shared.api.i18n.ILocalizedString;

/**
 * @author balandin
 * @since Apr 27, 2015 7:34:59 PM
 */
public interface LookupType extends ObjectType {

    public ILocalizedString getTitle();
}
