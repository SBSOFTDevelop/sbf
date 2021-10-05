package ru.sbsoft.client.components.grid.aggregate;

import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.shared.meta.Aggregate;
import ru.sbsoft.shared.meta.Row;

/**
 * Статистическая функция по заданным колонке и строкам браузера. 
 * @author Kiselev
 * @see AggregateWindow
 */
public interface IAggregate {

    /**
     * Вычисляет значение и возвращает его в отформатированном для отображения виде.
     * @param c колонка браузера
     * @param rows выбранные строки браузера
     * @return отформатированный результат вычисления
     * @throws IllegalArgumentException если колонка не поддерживается
     */
    String calc(CustomColumnConfig c, Iterable<Row> rows);

    /**
     * @return наименование функии для отображения пользователю
     */
    String getName();
    
    /**
     * @param <T> тип колонки
     * @param configClass
     * @return true, если на колонке возможна эта функция, false - иначе.
     */
    <T extends CustomColumnConfig> boolean isSupported(Class<T> configClass);
    
    /**
     * @return тип агрегирующей функции
     */
    Aggregate getKind();
}
