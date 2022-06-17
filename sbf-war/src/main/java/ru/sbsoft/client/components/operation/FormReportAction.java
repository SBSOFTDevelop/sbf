package ru.sbsoft.client.components.operation;

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
public class FormReportAction<M extends IFormModel> extends FormOperationAction<M> {

    public FormReportAction(BaseForm<M> form, OperationType type) {
        this(form, type, (IParamFormFactory) null);
    }

    public FormReportAction(BaseForm<M> form, OperationType type, boolean refeshOnComplete) {
        this(form, type, (IParamFormFactory) null, refeshOnComplete);
    }

    public FormReportAction(BaseForm<M> form, OperationType type, IParamFormFactory paramFormFactory) {
        this(form, type, paramFormFactory, true);
    }

    public FormReportAction(BaseForm<M> form, OperationType type, IParamFormFactory paramFormFactory, boolean refeshOnComplete) {
        super(form, type, paramFormFactory, refeshOnComplete, SBFResources.TREEMENU_ICONS.print16(), SBFResources.TREEMENU_ICONS.print24());
    }
}
