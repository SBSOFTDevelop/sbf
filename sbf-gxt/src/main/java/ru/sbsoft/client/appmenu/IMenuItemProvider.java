package ru.sbsoft.client.appmenu;

import ru.sbsoft.shared.model.MenuItemModel;

/**
 * Поставщик пункта меню
 *
 * @author Kiselev
 */
public interface IMenuItemProvider {

    /**
     * @return модель пункта меню
     */
    MenuItemModel getMenuItemModel();
}
