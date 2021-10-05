package ru.sbsoft.client.components.treemenu;

import ru.sbsoft.client.components.exception.BrowserException;
import ru.sbsoft.shared.model.MenuItemModel;

/**
 * Объект, умеющий запускать пункты меню на выполнение.
 * Реализация в каждом проекте.
 * Регистрируется в наследниках {@link ru.sbsoft.client.SBFEntryPoint}
 * @author panarin
 */
public interface IMenuExecutor {

    void executeItem(String appCode, MenuItemModel item) throws BrowserException;
}
