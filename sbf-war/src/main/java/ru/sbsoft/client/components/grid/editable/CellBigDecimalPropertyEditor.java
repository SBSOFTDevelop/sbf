package ru.sbsoft.client.components.grid.editable;

import com.google.gwt.i18n.client.NumberFormat;
import ru.sbsoft.svc.widget.core.client.form.NumberPropertyEditor;
import java.util.Objects;
import ru.sbsoft.shared.grid.format.IExpCellFormat;
import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author vk
 */
class CellBigDecimalPropertyEditor extends NumberPropertyEditor.BigDecimalPropertyEditor implements IGridRowDependent {

    private final IExpCellFormat expFormat;

    public CellBigDecimalPropertyEditor(IExpCellFormat expFormat) {
        Objects.requireNonNull(expFormat, IExpCellFormat.class.getName() + " must be provided for " + this.getClass().getName());
        this.expFormat = expFormat;
    }

    @Override
    public void setGridRow(Row row) {
        format = row != null ? NumberFormat.getFormat(expFormat.getFormat(row)) : null;
    }
}
