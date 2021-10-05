package ru.sbsoft.client.components.form;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

/**
 * @author balandin
 * @param <T>
 */
public class ComboBox<T> extends SimpleComboBox<T> {

    private HandlerRegistration selectImmediatelyReg = null;

    public ComboBox(ComboBoxCell<T> cell) {
        super(cell);
    }

    public ComboBox(LabelProvider<? super T> labelProvider) {
        super(labelProvider);
    }

    public final ComboBox<T> setSelectImmediately(boolean b) {
        if (b && selectImmediatelyReg == null) {
            selectImmediatelyReg = addSelectionHandler(new SelectionHandler<T>() {
                @Override
                public void onSelection(SelectionEvent<T> event) {
                    final Field<T> f = (Field<T>) event.getSource();
                    final T selectedItem = event.getSelectedItem();
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            f.setValue(selectedItem, true, true);
                        }
                    });
                }
            });
        } else if (!b && selectImmediatelyReg != null) {
            selectImmediatelyReg.removeHandler();
            selectImmediatelyReg = null;
        }
        return this;
    }

    /**
     *
     *
     * PUBLIC
     *
     *
     * @param force
     * @return
     */
    @Override
    public boolean redraw(boolean force) {
        // Комбо бокс по умолчанию не прорисовывает вручную установленное значение
        return super.redraw(force);
    }
}
