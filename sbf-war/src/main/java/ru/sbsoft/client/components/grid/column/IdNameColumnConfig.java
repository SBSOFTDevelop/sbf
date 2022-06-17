package ru.sbsoft.client.components.grid.column;

import ru.sbsoft.client.components.grid.column.cell.IdNameCell;
import ru.sbsoft.shared.kladr.AddressModel;
import ru.sbsoft.shared.meta.IColumn;
import ru.sbsoft.shared.util.IdName;

/**
 * Параметры колонки таблицы со значениями типа {@link AddressModel}.
 */
public class IdNameColumnConfig extends CustomColumnConfig<IdName<?>> {

    public IdNameColumnConfig(IColumn column, int index) {
        super(new CustomValueProvider<IdName<?>>(index), column);
        setCell(new IdNameCell());
        setSortable(false);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
