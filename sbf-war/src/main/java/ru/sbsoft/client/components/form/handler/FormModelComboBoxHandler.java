package ru.sbsoft.client.components.form.handler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import ru.sbsoft.svc.cell.core.client.form.ComboBoxCell;
import ru.sbsoft.svc.data.shared.LabelProvider;
import ru.sbsoft.svc.data.shared.ListStore;
import ru.sbsoft.svc.widget.core.client.form.ComboBox;
import java.math.BigDecimal;
import ru.sbsoft.client.components.form.model.IFormModelAccessor;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.filter.KeyFilterInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.interfaces.NamedItem;
import ru.sbsoft.shared.param.BigDecimalParamInfo;

/**
 *
 * @author Kiselev
 * @param <V>
 * @param <SelfType>
 */
public class FormModelComboBoxHandler<V extends IFormModel, SelfType extends FormModelComboBoxHandler<V, SelfType>> extends AbstractComboBoxHandler<ComboBox<V>, V, SelfType> {
    private static final IFormModelAccessor a = GWT.create(IFormModelAccessor.class);
    
    public FormModelComboBoxHandler(NamedItem it, LabelProvider<? super V> labelProvider) {
        super(it, labelProvider);
    }

    public FormModelComboBoxHandler(String name, String label, LabelProvider<? super V> labelProvider) {
        super(name, label, labelProvider);
    }

    @Override
    protected ComboBox<V> createField() {
        ListStore<V> st = new ListStore<>(a.key());
        ComboBox<V> cb = new ComboBox<>(st, labelProvider);
        cb.setAutoValidate(true);
        cb.setForceSelection(true);
        cb.setTypeAhead(true);
        cb.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        cb.setEditable(false);
        cb.addKeyUpHandler(event -> {
            if (cb.isAllowBlank()) {
                int code = event.getNativeKeyCode();
                if (code == KeyCodes.KEY_BACKSPACE || code == KeyCodes.KEY_DELETE) {
                    cb.setValue(null);
                }
            } 
        });
        return cb;
    }
    
    private BigDecimal getId(){
        return getVal() != null ? getVal().getId() : null;
    }

    @Override
    protected FilterInfo createFilter() {
        return new KeyFilterInfo(null, getId());
    }

    @Override
    protected ParamInfo createParamInfo() {
       return new BigDecimalParamInfo(null, getId());
    }
}
