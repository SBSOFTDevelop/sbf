package ru.sbsoft.client.components.grid.column.cell;

import ru.sbsoft.shared.util.IdName;

/**
 * Ячейка таблицы для отображения значений из заданного {@link IdName}.
 *
 */
public class IdNameCell extends CustomCell<IdName<?>> {

    @Override
    public String format(IdName<?> value) {
        return value != null ? value.getName() : null;
    }
}
