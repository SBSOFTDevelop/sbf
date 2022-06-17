package ru.sbsoft.client.components.form;

import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.client.components.*;
import ru.sbsoft.sbf.app.model.FormModel;

/**
 * Контейнер полей для формы с отложенной их инициализацией. Инициализация,
 * обычно, вызывается при отображении вкладки с контейнером или при сохранении.
 *
 * @param <T> модель данных
 */
public abstract class LazyPageForm<T extends FormModel>
        extends SimplePageFormContainer
        implements IHasParentElement {

    private T dataModel;
    private boolean initalized = false;
    private BaseForm ownerForm;

    private final DefaultContainerHolder containerHolder = new DefaultContainerHolder();

    @Override
    public void setParentElement(IElementContainer parent) {
        containerHolder.setParentElement(parent);
    }

    @Override
    public IElementContainer getParentElement() {
        return containerHolder.getParentElement();
    }

    public LazyPageForm() {
        this(true);
    }

    public LazyPageForm(int labelWidth) {
        this(labelWidth, true);
    }

    public LazyPageForm(boolean initScroll) {
        this(DEFAULT_LABEL_WIDTH, initScroll);
    }

    public LazyPageForm(int labelWidth, boolean initScroll) {
        super(labelWidth, initScroll);
    }

    public boolean isNotInitalized() {
        return !initalized;
    }

    public boolean isInitalized() {
        return initalized;
    }

    public String getContextName(String context) {
        return getContextName(context, dataModel);
    }

    public String getContextName(String context, T dataModel) {
        return (null == dataModel || null == dataModel.getId()) ? context : context + dataModel.getId().toString();
    }

    public BaseForm getOwnerForm() {
        if (ownerForm == null) {
            ownerForm = findForm();
        }
        return ownerForm;
    }

    public BaseForm findForm() {
        return ElementUtils.findForm((Widget) this);
    }

    @Override
    protected void onShow() {
        final BaseForm form = getOwnerForm();
        if ((!initalized) && (null != form)) {
            final boolean changed = form.isChanged();
            initEditors();
            form.setChanged(changed);
            form.refreshFormGridViews(this);
        }
        super.onShow();
    }

    protected void initEditors() {
        if (!initalized) {
            initalized = true;
            createEditors();
            final BaseForm form = getOwnerForm();
            if (form != null) {
                form.updateFieldsInfo(this);
            }
            if (dataModel != null) {
                dataToPage(dataModel);
            }
            if (form != null) {
                form.updateState(true);
            }
            updateLabels();
            if (getOwnerForm() != null && getOwnerForm().isReadOnly()) {
                form.updateReadOnly(this, true);
            }
        }
    }

    public void setReadOnlyGroup(boolean readOnly) {
        final BaseForm form = getOwnerForm();
        if (form == null) {
            throw new IllegalStateException("Owner form not found");
        }
        form.setReadOnlyGroup(this, readOnly);
    }

    public void setReadOnly(Component component, boolean readOnly) {
        final BaseForm form = getOwnerForm();
        if (form == null) {
            throw new IllegalStateException("Owner form not found");
        }
        form.findAdapter(component).setTemporaryReadOnly(readOnly);
    }

    protected abstract void createEditors();

    public final void dataToPage(T dataModel) {
        setDataModel(dataModel);
        if (isInitalized()) {
            dataToForm(dataModel);
        }
    }

    public final void pageToData(T dataModel) {
        if (isInitalized() && null != dataModel) {
            formToData(dataModel);
        }
    }

    protected abstract void dataToForm(T dataModel);

    protected abstract void formToData(T dataModel);

    public T getDataModel() {
        return dataModel;
    }

    public void setDataModel(T dataModel) {
        this.dataModel = dataModel;
    }
}
