package ru.sbsoft.client.components.form;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.sbsoft.client.components.form.handler.param.StrStrComboBoxHandler;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.sbf.app.model.FormModel;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.consts.Dict;
import ru.sbsoft.shared.interfaces.NamedItem;
import ru.sbsoft.shared.meta.Row;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kiselev
 * @param <E>
 */
public class SelectorForm<E extends NamedItem> implements IMultiFormFactory<FormModel, Row> {

    private final static String DEFAULT_COLUMN_NAME = Dict.DTYPE;

    private String columnName = DEFAULT_COLUMN_NAME;
    private final Map<String, IFormFactory<? extends FormModel, Row>> forms = new HashMap<>();
    protected final ChoiceForm<E> choiceForm;
    private BaseGrid ownerGrid = null;
    private E newItemType = null;

    public SelectorForm(String header, String label) {
        choiceForm = new ChoiceForm<E>(header, label);
    }

    public SelectorForm<E> addForm(E key, IFormFactory<? extends FormModel, Row> form) {
        forms.put(key.getCode(), form);
        choiceForm.addItem(key);
        return this;
    }

    public SelectorForm<E> setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    @Override
    public void setOwnerGrid(BaseGrid ownerGrid) {
        this.ownerGrid = ownerGrid;
    }

    public BaseGrid getOwnerGrid() {
        return ownerGrid;
    }

    public SelectorForm setNewItemType(E newItemType) {
        this.newItemType = newItemType;
        return this;
    }

    @Override
    public void createEditForm(Row model, AsyncCallback<BaseForm<FormModel>> callback) {
        try {
            IFormFactory<FormModel, Row> factory = (IFormFactory<FormModel, Row>) (model != null ? getForm(model) : getForm(newItemType));
            if (factory != null) {
                factory.createEditForm(model, new AsyncCallback<BaseForm<FormModel>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(BaseForm<FormModel> result) {
                        callback.onSuccess(result);
                    }
                });
            } else {
                choiceForm.makeChoice(callback);
            }
        } catch (Throwable ex) {
            callback.onFailure(ex);
        }
    }

    @Override
    public FormContext getFormContext(Row model) {
        return model != null ? getForm(model).getFormContext(model) : null;
    }

    protected IFormFactory<? extends FormModel, Row> getForm(E ftype) {
        return ftype != null ? getForm(ftype.getCode()) : null;
    }

    private IFormFactory<? extends FormModel, Row> getForm(Row model) {
        String ftype = model.getString(columnName);
        if (ftype == null) {
            throw new IllegalArgumentException("Type value in column '" + columnName + "' is null");
        }
        return getForm(ftype);
    }

    private IFormFactory<? extends FormModel, Row> getForm(String ftype) {
        if (ftype == null) {
            throw new IllegalArgumentException("Type value can't be null");
        }
        IFormFactory<? extends FormModel, Row> f = forms.get(ftype);
        if (f == null) {
            throw new IllegalArgumentException("Form type '" + ftype + "' is not registered");
        }
        return f;
    }

    @Override
    public String createFormKeyFromModel(Row selectedModel) {
        if (null == selectedModel) {
            return "SelectorForm";
        } else {
            return selectedModel.getString(columnName);
        }
    }

    protected class ChoiceForm<E1 extends NamedItem> extends GeneralParamForm {

        private static final String PARAM_NAME = "choice";
        private final StrStrComboBoxHandler selectHandler;
        private AsyncCallback<BaseForm<FormModel>> choiceCallback;

        public ChoiceForm(String header, String label) {
            super(header);
            selectHandler = new StrStrComboBoxHandler(PARAM_NAME, label).setReq(true).setToolTip(header);
        }

        public ChoiceForm<E1> cleanItems() {
            selectHandler.cleanItems();
            return this;
        }

        public ChoiceForm<E1> addItems(Collection<E1> es) {
            selectHandler.setNamedItems(es);
            return this;
        }

        public ChoiceForm<E1> addItem(E1 e) {
            selectHandler.setItems(e);
            return this;
        }

        @Override
        protected void addHandlers(ParamHandlerCollector hc) {
            hc.add(selectHandler);
        }

        public void makeChoice(AsyncCallback<BaseForm<FormModel>> callback) {
            choiceCallback = callback;
            show();
        }

        @Override
        public void hide() {
            super.hide();
            if (isNeedStart() && choiceCallback != null) {
                AsyncCallback<BaseForm<FormModel>> cb = choiceCallback;
                choiceCallback = null;
                IFormFactory<FormModel, Row> factory = (IFormFactory<FormModel, Row>) getForm(selectHandler.getVal());
                factory.createEditForm(null, cb);
            }
        }
    }
}
