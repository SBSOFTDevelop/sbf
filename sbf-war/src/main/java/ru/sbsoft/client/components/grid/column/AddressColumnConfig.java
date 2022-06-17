package ru.sbsoft.client.components.grid.column;

import ru.sbsoft.client.components.grid.column.cell.CustomCell;
import ru.sbsoft.shared.kladr.AddressModel;
import ru.sbsoft.shared.meta.IColumn;

/**
 * Параметры колонки таблицы со значениями типа {@link AddressModel}.
 */
public class AddressColumnConfig extends CustomColumnConfig<AddressModel> {

    public AddressColumnConfig(IColumn column, int index) {
        super(new CustomValueProvider<AddressModel>(index), column);
        setCell(new CustomCell<>());
        setSortable(false);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
