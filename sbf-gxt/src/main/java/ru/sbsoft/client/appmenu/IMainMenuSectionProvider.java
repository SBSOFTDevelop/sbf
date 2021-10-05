package ru.sbsoft.client.appmenu;

import ru.sbsoft.shared.model.ApplicationMenuTreeModel;

/**
 * Поставщик раздела главного меню приложения
 * @author Kiselev
 */
public interface IMainMenuSectionProvider {

    /**
     * @return модель раздела
     */
    ApplicationMenuTreeModel getMenuSectionModel();
}
