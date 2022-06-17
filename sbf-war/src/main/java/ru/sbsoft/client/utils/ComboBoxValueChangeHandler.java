package ru.sbsoft.client.utils;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.widget.core.client.form.ComboBox;
import ru.sbsoft.svc.widget.core.client.form.Field;

public abstract class ComboBoxValueChangeHandler<T> implements ValueChangeHandler<T>, SelectionHandler<T> {

    @Override
    public abstract void onValueChange(ValueChangeEvent<T> event);

    @Override
    public void onSelection(SelectionEvent<T> event) {
        final Field<T> f = (Field<T>) event.getSource();
        final T selectedItem = event.getSelectedItem();
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            @Override
            public void execute() {
                f.setValue(selectedItem, true, true);
                //f.finishEditing();
            }
        });
    }

    public ComboBoxValueChangeHandler<T> addHandler(ComboBox<T> comboBox) {
        HandlerRegistration addValueChangeHandler = comboBox.addValueChangeHandler(this);
        comboBox.setData("~h1", addValueChangeHandler);
        comboBox.setData("~h2", comboBox.addSelectionHandler(this));
        return this;
    }

    public void unregistrHandler(ComboBox<T> comboBox) {
        unregistrHandler(comboBox, "~h1");
        unregistrHandler(comboBox, "~h2");
    }

    private void unregistrHandler(ComboBox<T> comboBox, String alias) {
        HandlerRegistration h = (HandlerRegistration) comboBox.getData(alias);
        if (h != null) {
            h.removeHandler();
        }
    }
}
