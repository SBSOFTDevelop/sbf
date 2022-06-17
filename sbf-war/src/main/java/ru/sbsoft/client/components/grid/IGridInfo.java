package ru.sbsoft.client.components.grid;

import ru.sbsoft.shared.meta.IColumns;

/**
 * Предоставляет информацию о конфигурации таблицы браузера.
 * @author Kiselev
 */
public interface IGridInfo {
    IColumns getMetaInfo();
}
