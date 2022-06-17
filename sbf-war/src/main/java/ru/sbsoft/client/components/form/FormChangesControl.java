package ru.sbsoft.client.components.form;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.widget.core.client.form.TextArea;
import ru.sbsoft.svc.widget.core.client.form.TextField;
import ru.sbsoft.svc.widget.core.client.form.ValueBaseField;
import java.util.HashMap;
import java.util.Map;
import ru.sbsoft.client.components.actions.event.ChangedStateEvent;
import ru.sbsoft.client.components.actions.event.ChangedStateHandler;

/**
 * Сохраняет и обновляет флаг наличия изменений на форме данных.
 *
 * @see BaseForm
 */
public class FormChangesControl implements HasValueChangeHandlers<Boolean> {

    private final Handler changeHandler = new Handler();
    private final MutableHandler mutableChangeHandler = new MutableHandler();
    private final HandlerManager handlerManager;
    private final Map<ValueBaseField<String>, String> valMap = new HashMap<>();
    private final Map<HasChangeState, Boolean> individualChahged = new HashMap<>();
    //
    private boolean changed = false;
    private boolean lastChanged = false;

    class MutableHandler implements ChangedStateHandler {

        @Override
        public void updateState(ChangedStateEvent ev) {
            HasChangeState hh = (HasChangeState) ev.getSource();
            if (individualChahged.containsKey(hh)) {
                individualChahged.put(hh, ev.isChanged());
                updateChanged();
            }
        }
    }

    class Handler implements SelectionHandler, ValueChangeHandler, ChangeHandler {

        @Override
        public void onSelection(SelectionEvent event) {
            event(event);
        }

        @Override
        public void onValueChange(ValueChangeEvent event) {
            event(event);
        }

        @Override
        public void onChange(ChangeEvent event) {
            event(event);
        }

        private void event(GwtEvent event) {
            boolean realChange = true;
            ValueBaseField<String> f = getTextField(event.getSource());
            if (f != null) {
                if (valMap.containsKey(f) && equals(valMap.get(f), f.getValue())) {
                    realChange = false;
                }
                valMap.put(f, f.getValue());
            }
            if (realChange) {
                setChanged(true);
            }
        }

        private String norm(String s) {
            return s == null || s.trim().isEmpty() ? null : s;
        }

        private boolean equals(String s1, String s2) {
            s1 = norm(s1);
            s2 = norm(s2);
            if (s1 == s2) {
                return true;
            }
            if (s1 == null || s2 == null) {
                return false;
            }
            return s1.equals(s2);
        }
    }

    public FormChangesControl() {
        handlerManager = new HandlerManager(this);
    }

    private ValueBaseField<String> getTextField(Object o) {
        if ((o instanceof TextField) || (o instanceof TextArea)) {
            return (ValueBaseField<String>) o;
        }
        return null;
    }

    /**
     * Устанавливает на {@link Widget} слушатель изменений.
     *
     * @param widget
     */
    public void registr(final Widget widget) {
        if (widget instanceof HasSelectionHandlers) {
            ((HasSelectionHandlers) widget).addSelectionHandler(changeHandler);
        }
        if (widget instanceof HasChangeState) {
            HasChangeState hh = (HasChangeState) widget;
            individualChahged.put(hh, hh.isChanged());
            hh.addChangedStateHandler(mutableChangeHandler);
        } else {
            if (widget instanceof HasChangeHandlers) {
                ((HasChangeHandlers) widget).addChangeHandler(changeHandler);
            }
            if (widget instanceof HasValueChangeHandlers) {
                ((HasValueChangeHandlers) widget).addValueChangeHandler(changeHandler);
            }
        }
        ValueBaseField<String> f = getTextField(widget);
        if (f != null) {
            valMap.put(f, f.getValue());
        }
    }

    public boolean isChanged() {
        return changed || individualChahged.values().stream().anyMatch(b -> (Boolean.TRUE.equals(b)));
    }

    private void updateInitValues() {
        valMap.entrySet().forEach(e -> {
            e.setValue(e.getKey().getValue());
        });
        individualChahged.keySet().forEach(h -> {
            h.clearChanges();
            individualChahged.put(h, Boolean.FALSE);
        });
    }

    private void updateChanged() {
        if (this.lastChanged != isChanged()) {
            this.lastChanged = isChanged();
            ValueChangeEvent.fire(this, this.lastChanged);
        }
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
        if (!changed) {
            updateInitValues();
        }
        updateChanged();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
        return handlerManager.addHandler(ValueChangeEvent.getType(), handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }
}
