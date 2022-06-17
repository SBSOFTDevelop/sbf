package ru.sbsoft.client.components.operation;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.client.components.form.BaseValidatedForm;
import ru.sbsoft.client.components.form.IParentFormAware;
import ru.sbsoft.sbf.app.model.FormModel;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.model.GridParamsBean;
import ru.sbsoft.shared.model.operation.OperationCommand;

import java.math.BigDecimal;
import java.util.Collections;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public class FormOperation<M extends FormModel> extends AbstractOperation {

    private final OperationType type;
    private final BaseForm<M> form;
    private final boolean refeshOnComplete;

    public FormOperation(OperationType type, BaseForm<M> form) {
        this(type, form, null);
    }

    public FormOperation(OperationType type, BaseForm<M> form, boolean refeshOnComplete) {
        this(type, form, null, refeshOnComplete);
    }

    public FormOperation(OperationType type, BaseForm<M> form, BaseOperationParamForm paramForm) {
        this(type, form, paramForm, false);
    }

    public FormOperation(OperationType type, BaseForm<M> form, BaseOperationParamForm paramForm, boolean refeshOnComplete) {
        this.type = type;
        this.form = form;
        this.refeshOnComplete = refeshOnComplete;
        if (paramForm != null) {
            paramForm.setHeading(I18n.get(type.getTitle()));
            if(paramForm instanceof IParentFormAware){
                ((IParentFormAware)paramForm).setParentForm(form);
            }
            setParamWindow(paramForm);
        }
        
    }

    @Override
    protected OperationCommand createOperationCommand() {
        OperationCommand cmd = new OperationCommand(type);
        BigDecimal id = form.getRecordUQ();
        if (id != null) {
            //cmd.setGridContext(new GridParamsBean(null, null, Arrays.asList(id), id));
            cmd.setGridContext(new GridParamsBean(null, null, Collections.singletonList(id), id));

        }
        return cmd;
    }

    @Override
    public void onClose(Long operationId) {
        super.onClose(operationId);
        if (refeshOnComplete) {
            if (form instanceof BaseValidatedForm) {
                ((BaseValidatedForm)form).refreshWithParentGrid();
            } else {
                form.refresh();
                if (form.getRecordUQ() != null) {
                    form.getOwnerGrid().refreshRow(form.getRecordUQ());
                }
            }
        }

    }

}
