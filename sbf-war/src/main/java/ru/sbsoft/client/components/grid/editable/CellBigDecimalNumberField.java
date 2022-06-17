package ru.sbsoft.client.components.grid.editable;

import ru.sbsoft.svc.widget.core.client.form.NumberField;
import ru.sbsoft.svc.widget.core.client.form.PropertyEditor;
import java.math.BigDecimal;
import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author vk
 */
class CellBigDecimalNumberField extends NumberField<BigDecimal> implements IGridRowDependent {

    public CellBigDecimalNumberField(CellBigDecimalPropertyEditor editor) {
        super(editor);
    }

    @Override
    public CellBigDecimalPropertyEditor getPropertyEditor() {
        return (CellBigDecimalPropertyEditor)super.getPropertyEditor();
    }

    @Override
    public void setPropertyEditor(PropertyEditor<BigDecimal> propertyEditor) {
        if(!(propertyEditor instanceof CellBigDecimalPropertyEditor)){
            throw new IllegalArgumentException("Only " + CellBigDecimalPropertyEditor.class.getName() + " is supported for " + this.getClass().getName());
        }
        super.setPropertyEditor(propertyEditor);
    }

    @Override
    public void setGridRow(Row row) {
        getPropertyEditor().setGridRow(row);
    }
}
