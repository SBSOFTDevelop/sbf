package ru.sbsoft.client.components.grid.column;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.LookupItem;
import ru.sbsoft.client.components.form.LookupItemsList;
import ru.sbsoft.client.components.grid.column.cell.LookupCell;
import ru.sbsoft.client.filter.editor.FilterAdapter;
import ru.sbsoft.client.filter.editor.LookupFilterAdapter;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.meta.IColumn;
import ru.sbsoft.shared.renderer.Renderer;

/**
 * Параметры колонки таблицы для которой задан свой {@link Renderer}.
 */
public class LookupColumnConfig extends CustomColumnConfig<Object> {

    private final Renderer renderer;

    public LookupColumnConfig(IColumn column, int index, Renderer renderer) {
        super(new CustomValueProvider<Object>(index), column);
        this.renderer = renderer;

        setCell(new LookupCell<Object>(fill(false), renderer));
        switch (renderer.getTextAlign()) {
            case center:
                setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
                break;
            case left:
                setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
                break;
            case right:
                setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
                break;
            case end:
                setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LOCALE_END);
                break;
            case justify:
                setHorizontalAlignment(HasHorizontalAlignment.ALIGN_JUSTIFY);
                break;
            case start:
                setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LOCALE_START);
            default:
                throw new AssertionError();
        }

    }

    private LookupItemsList fill(boolean extended) {
        LookupItemsList result = new LookupItemsList();
        List<Renderer.Entry> items = renderer.getItems();
        for (Renderer.Entry entry : items) {
            String label = Strings.clean(extended ? I18n.get(entry.getExtendValue()) : I18n.get(entry.getValue()), true);
            result.add(new LookupItem(entry.getKey(), label == null ? "-" : label));
        }
        return result;
    }

    @Override
    public FilterAdapter createFilterAdapter() {
        return new LookupFilterAdapter(getColumn().getType().getFilterTypeEnum(), getColumn().getFilterConditions(), fill(true));
    }
}
