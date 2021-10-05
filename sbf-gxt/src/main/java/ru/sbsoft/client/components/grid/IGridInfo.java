package ru.sbsoft.client.components.grid;

import ru.sbsoft.shared.meta.Columns;

/**
 * Предоставляет информацию о конфигурации таблицы браузера.
 * @author Kiselev
 */
public interface IGridInfo {
    Columns getMetaInfo();
}
