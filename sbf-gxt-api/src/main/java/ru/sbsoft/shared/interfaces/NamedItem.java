package ru.sbsoft.shared.interfaces;

import ru.sbsoft.shared.api.i18n.ILocalizedString;

/**
 * Интерфейс представления кодового элемента с наименованием.
 *
 * @author Kiselev
 */
public interface NamedItem extends ObjectType {

    ILocalizedString getItemName();
}
