package ru.sbsoft.client.components.grid.column.cell;

import ru.sbsoft.client.components.form.LookupItem;
import ru.sbsoft.client.components.form.LookupItemsList;
import ru.sbsoft.shared.renderer.Renderer;

/**
 * Ячейка таблицы для отображения значений из заданного {@link LookupStore}.
 *
 * @author balandin
 * @since Oct 23, 2014 2:49:59 PM
 */
public class LookupCell<N> extends CustomCell<N> {

    private final LookupItemsList store;
    private final Renderer renderer;

    public LookupCell(LookupItemsList store, Renderer renderer) {
        this.store = store;
        this.renderer = renderer;
    }

    @Override
    public String format(N value) {
        if (value == null) {
            return null;
        }
        final LookupItem item = store.findLookupItem(value);
        if (item == null) {
            return  renderer.getDefaultValue(value);// "[" + value + "]";
        }
        return String.valueOf(item.getValue());
    }
}
