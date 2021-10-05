package ru.sbsoft.client.components.browser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.client.components.form.CustomReportFormFactory;
import ru.sbsoft.client.components.grid.ContextGrid;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.NamedFormContext;
import ru.sbsoft.shared.consts.SBFGridEnum;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.model.CustomReportModel;

/**
 *
 * @author sokolov
 */
public class CustomReportGrid extends ContextGrid {

    private final CustomReportFormFactory formFactory;

    public CustomReportGrid(NamedFormContext formContext) {
        super(SBFGridEnum.SR_CUSTOMREPORT);
        this.formFactory = new CustomReportFormFactory(formContext);
    }

    @Override
    public FormContext getFormContext(Row model) {
        return formFactory.getFormContext(model);
    }

    @Override
    protected void createEditForm(Row model, AsyncCallback<BaseForm> callback) {
        formFactory.createEditForm(model, new AsyncCallback<BaseForm<CustomReportModel>>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(BaseForm<CustomReportModel> result) {
                callback.onSuccess(result);
            }
        });
    }
}
