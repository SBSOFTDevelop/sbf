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
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.ValueBaseField;
import java.util.HashMap;
import java.util.Map;

/**
 * Сохраняет и обновляет флаг наличия изменений на форме данных.
 *
 * @see BaseForm
 */
public class FormChangesControl implements HasValueChangeHandlers<Boolean> {

    private final Handler changeHandler = new Handler();
    private final HandlerManager handlerManager;
    private Map<ValueBaseField<String>, String> valMap = new HashMap<ValueBaseField<String>, String>();
    //
    private boolean changed = false;

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
//            final FieldLabel l = findFieldLabel((Component) event.getSource());
//            if (l != null) {
//                Info.display("", "" + l.getText());
//            } else {
//                Info.display("", "хз");
//            }
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

//        public FieldLabel findFieldLabel(Component f) {
//            Widget c = f;
//            int i = 0;
//            while ((c = c.getParent()) != null && ++i < 5) {
//                if (c instanceof FieldLabel) {
//                    return (FieldLabel) c;
//                }
//            }
//            return null;
//        }
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
        if (widget instanceof HasChangeHandlers) {
            ((HasChangeHandlers) widget).addChangeHandler(changeHandler);
        }
        if (widget instanceof HasValueChangeHandlers) {
            ((HasValueChangeHandlers) widget).addValueChangeHandler(changeHandler);
        }
        ValueBaseField<String> f = getTextField(widget);
        if (f != null) {
            valMap.put(f, f.getValue());
        }
    }

    public boolean isChanged() {
        return changed;
    }
    
    private void updateInitValues(){
        for(Map.Entry<ValueBaseField<String>, String> e : valMap.entrySet()){
            e.setValue(e.getKey().getValue());
        }
    }

    public void setChanged(boolean changed) {
        if (this.changed != changed) {
            this.changed = changed;
            ValueChangeEvent.fire(this, this.changed);
        }
        if(!changed){
            updateInitValues();
        }
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
