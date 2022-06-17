package ru.sbsoft.client.components.operation;

import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.client.components.SBSelectHandler;
import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.client.components.form.action.FormAction;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.exceptions.ApplicationException;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public abstract class FormAbstractAction<M extends IFormModel> extends FormAction {

    private final SBSelectHandler selectHandler;
    private boolean enabled = true;
    private boolean restrictUnsaved = true;
    private String objectName = null;

    protected FormAbstractAction(BaseForm<M> form, String caption, ImageResource icon16, ImageResource icon24) {
        this(form, caption, caption, icon16, icon24);
    }

    protected FormAbstractAction(BaseForm<M> form, String caption, String tooltip, ImageResource icon16, ImageResource icon24) {
        super(form);
        setCaption(caption);
        setToolTip(tooltip);
        setIcon16(icon16);
        setIcon24(icon24);
        this.selectHandler = new SBSelectHandler() {
            @Override
            public void onSelectEvent() {
                onExecute();
            }
        };
    }

    public FormAbstractAction setObjectName(String objectName) {
        this.objectName = objectName;
        return this;
    }

    public FormAbstractAction setRestrictUnsaved(boolean restrictUnsaved) {
        this.restrictUnsaved = restrictUnsaved;
        return this;
    }

    protected void checkChanged() {
        if (getForm().getRecordUQ() == null) {
            if (objectName != null) {
                throw new ApplicationException(SBFExceptionStr.objectNotSaved, objectName);
            } else {
                throw new ApplicationException(SBFExceptionStr.newObjectNotSaved);
            }
        }
        if (restrictUnsaved && getForm().isChanged()) {
            throw new ApplicationException(SBFExceptionStr.querySaveCancel);
        }
    }

    public SBSelectHandler getSelectHandler() {
        return selectHandler;
    }

    public void setEnabled(boolean b) {
        this.enabled = b;
    }

    @Override
    public boolean checkEnabled() {
        return enabled && getForm() != null && getForm().getRecordUQ() != null && !(restrictUnsaved && getForm().isChanged());
    }

    @Override
    public BaseForm<M> getForm() {
        return super.getForm();
    }
}
