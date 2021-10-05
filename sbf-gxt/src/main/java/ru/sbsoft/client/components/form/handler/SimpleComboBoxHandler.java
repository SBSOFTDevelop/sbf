package ru.sbsoft.client.components.form.handler;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author Kiselev
 * @param <V>
 * @param <SelfType>
 */
public class SimpleComboBoxHandler<V, SelfType extends SimpleComboBoxHandler<V, SelfType>> extends AbstractComboBoxHandler<SimpleComboBox<V>, V, SelfType> {

    public SimpleComboBoxHandler(NamedItem it, LabelProvider<? super V> labelProvider) {
        super(it, labelProvider);
    }

    public SimpleComboBoxHandler(String name, String label, LabelProvider<? super V> labelProvider) {
        super(name, label, labelProvider);
    }

    @Override
    protected SimpleComboBox<V> createField() {
        SimpleComboBox<V> cb = new SimpleComboBox<>(labelProvider);
        cb.setAutoValidate(true);
        cb.setForceSelection(true);
        cb.setTypeAhead(true);
        cb.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        cb.setEditable(false);
        return cb;
    }

    @Override
    protected FilterInfo createFilter() {
        throw new UnsupportedOperationException("SimpleComboboxHandler is not supported for filters");
    }

    @Override
    protected ParamInfo createParamInfo() {
        throw new UnsupportedOperationException("SimpleComboboxHandler is not supported for perameners");
    }
}
