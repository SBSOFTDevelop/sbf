package ru.sbsoft.client.components.form;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreClearEvent;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;
import com.sencha.gxt.widget.core.client.form.DualListField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.AllowBlankControl;
import ru.sbsoft.client.components.form.fields.ReadOnlyControl;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 *
 * @author Kiselev
 */
public class DualListEditField<M, T> extends DualListField<M, T> implements HasValueChangeHandlers<List<M>>, AllowBlankControl, ReadOnlyControl {

    private List<ValueChangeHandler<List<M>>> changeHandlers = new ArrayList<ValueChangeHandler<List<M>>>();
    private boolean allwBlank = true;
    private ButtonMessages msgs = null;

    public DualListEditField(ListStore<M> fromStore, ListStore<M> toStore, ValueProvider<? super M, T> valueProvider, Cell<T> cell) {
        super(fromStore, toStore, valueProvider, cell);


        DataChangeListener changeListener = new DataChangeListener();
        toStore.addStoreClearHandler(changeListener);
        toStore.addStoreDataChangeHandler(changeListener);
        toStore.addStoreAddHandler(changeListener);
        toStore.addStoreRemoveHandler(changeListener);
        addValidator(new Validator<List<M>>() {
            @Override
            public List<EditorError> validate(Editor<List<M>> editor, List<M> value) {
                if (!allwBlank && DualListEditField.this.getToStore().getAll().isEmpty()) {
                    return Collections.<EditorError>singletonList(new DefaultEditorError(editor, I18n.get(SBFGeneralStr.msgNeedFill), null));
                }
                return null;
            }
        });

    }

    @Override
    public DualListFieldMessages getMessages() {
        if (msgs == null) {
            msgs = new ButtonMessages();
        }
        return msgs;
    }

    @Override
    public boolean isReadOnly() {
        return !isEnabled();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        setEnabled(!readOnly);
    }

    private class DataChangeListener implements StoreClearEvent.StoreClearHandler<M>, StoreDataChangeEvent.StoreDataChangeHandler<M>, StoreAddEvent.StoreAddHandler<M>, StoreRemoveEvent.StoreRemoveHandler<M> {

        @Override
        public void onClear(StoreClearEvent<M> event) {
            fireValueChanged();
        }

        @Override
        public void onDataChange(StoreDataChangeEvent<M> event) {
            fireValueChanged();
        }

        @Override
        public void onAdd(StoreAddEvent<M> event) {
            fireValueChanged();
        }

        @Override
        public void onRemove(StoreRemoveEvent<M> event) {
            fireValueChanged();
        }

    }

    private void fireValueChanged() {
        ValueChangeEvent<List<M>> e = new ValueChangeEvent<List<M>>(new ArrayList<M>(getToStore().getAll())) {
        };
        for (ValueChangeHandler<List<M>> h : changeHandlers) {
            h.onValueChange(e);
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<M>> handler) {
        if (!changeHandlers.contains(handler)) {
            changeHandlers.add(handler);
        }
        return new HandlerRegistrationImpl(handler);
    }

    @Override
    public void setAllowBlank(boolean value) {
        this.allwBlank = value;
    }

    @Override
    public boolean isAllowBlank() {
        return allwBlank;
    }

    @Override
    protected void onRight() {
        if (!isReadOnly()) {
            super.onRight();
        }
    }

    @Override
    protected void onLeft() {
        if (!isReadOnly()) {
            super.onLeft();
        }
    }

    @Override
    protected void onAllRight() {
        if (!isReadOnly()) {
            super.onAllRight();
        }
    }

    @Override
    protected void onAllLeft() {
        if (!isReadOnly()) {
            super.onAllLeft();
        }
    }

    private class HandlerRegistrationImpl implements HandlerRegistration {

        private final ValueChangeHandler<List<M>> h;

        public HandlerRegistrationImpl(ValueChangeHandler<List<M>> h) {
            this.h = h;
        }

        @Override
        public void removeHandler() {
            changeHandlers.remove(h);
        }

    }

    private class ButtonMessages implements DualListFieldMessages {

        @Override
        public String addAll() {
            return I18n.get(SBFGeneralStr.labelAddAll);
        }

        @Override
        public String addSelected() {
            return I18n.get(SBFGeneralStr.labelAddSelected);
        }

        @Override
        public String moveDown() {
            return I18n.get(SBFGeneralStr.labelMoveDown);
        }

        @Override
        public String moveUp() {
            return I18n.get(SBFGeneralStr.labelMoveUp);
        }

        @Override
        public String removeAll() {
            return I18n.get(SBFGeneralStr.labelDeleteAll);
        }

        @Override
        public String removeSelected() {
            return I18n.get(SBFGeneralStr.labelDeleteSelected);
        }

    }
}
