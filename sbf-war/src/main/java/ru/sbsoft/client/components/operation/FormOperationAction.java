package ru.sbsoft.client.components.operation;

import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.client.components.form.IParamFormFactory;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.interfaces.OperationType;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public class FormOperationAction<M extends IFormModel> extends FormAbstractAction<M> {

    private final OperationType type;
    private final IParamFormFactory paramFormFactory;
    private final boolean refeshOnComplete;

    public FormOperationAction(BaseForm<M> form, OperationType type) {
        this(form, type, (IParamFormFactory) null);
    }

    public FormOperationAction(BaseForm<M> form, OperationType type, boolean refeshOnComplete) {
        this(form, type, (IParamFormFactory) null, refeshOnComplete);
    }

    public FormOperationAction(BaseForm<M> form, OperationType type, IParamFormFactory paramFormFactory) {
        this(form, type, paramFormFactory, true);
    }

    public FormOperationAction(BaseForm<M> form, OperationType type, IParamFormFactory paramFormFactory, boolean refeshOnComplete) {
        this(form, type, paramFormFactory, refeshOnComplete, SBFResources.GENERAL_ICONS.RunCurrent16(), SBFResources.GENERAL_ICONS.RunCurrent());
    }

    protected FormOperationAction(BaseForm<M> form, OperationType type, IParamFormFactory paramFormFactory, boolean refeshOnComplete, ImageResource icon16, ImageResource icon24) {
        super(form, I18n.get(type.getTitle()), icon16, icon24);
        this.type = type;
        this.paramFormFactory = paramFormFactory;
        this.refeshOnComplete = refeshOnComplete;
    }

    @Override
    public FormOperationAction setRestrictUnsaved(boolean restrictUnsaved) {
        super.setRestrictUnsaved(restrictUnsaved);
        return this;
    }

    protected BaseOperationParamForm createParamForm() {
        if (paramFormFactory != null) {
            return paramFormFactory.createForm();
        }
        return null;
    }

    protected OperationType getType() {
        return type;
    }

    protected boolean isRefeshOnComplete() {
        return refeshOnComplete;
    }
    
    protected AbstractOperation createOperation(){
        return new FormOperation(type, getForm(), createParamForm(), refeshOnComplete);
    }
    
    @Override
    public void onExecute() {
        checkChanged();
        createOperation().startOperation();
    }
}
