package ru.sbsoft.client.components.grid.column;

import ru.sbsoft.shared.meta.IColumn;

/**
 * Параметры колонки таблицы с числовыми значениями типа {@link BigDecimal}, представляющими собой ключевые поля.
 * @author vk
 */
public class KeyColumnConfig extends CurrencyColumnConfig {
    
    public KeyColumnConfig(IColumn column, int index) {
        super(column, index, "0");
    }
}
