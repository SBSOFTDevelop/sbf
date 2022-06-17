package ru.sbsoft.client.components.browser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.client.components.form.SchedulerForm;
import ru.sbsoft.client.components.grid.ContextGrid;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.shared.ComparisonEnum;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.TemplateParameterConst;
import ru.sbsoft.shared.consts.SBFGridEnum;
import ru.sbsoft.shared.filter.StringFilterInfo;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.services.FetchParams;

/**
 * Табличное представление жкрнала планировщиков операций.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class SchedulerGrid extends ContextGrid {

    private final FormContext formContext;

    public SchedulerGrid(FormContext formContext) {
        super(SBFGridEnum.SR_SCHEDULER);
        this.formContext = formContext;
    }

    @Override
    protected FormContext getFormContext(Row model) {
        return this.formContext;
    }

    @Override
    protected void createEditForm(Row model, AsyncCallback<BaseForm> callback) {
        try {
            final SchedulerForm f = new SchedulerForm(getFormContext(model));
            callback.onSuccess(f);
        } catch (Throwable ex) {
            callback.onFailure(ex);
        }
    }

    @Override
    public PageFilterInfo getFetchParams(FetchParams bean) {
        final PageFilterInfo result = super.getFetchParams(bean);
        List<FilterInfo> tmp = new ArrayList<>(result.getParentFilters());
        tmp.add(new StringFilterInfo(TemplateParameterConst.CURRENT_MODULE_CODE_PARAMETER, ComparisonEnum.eq, GWT.getModuleName()));
        result.setParentFilters(tmp);
        return result;
    }
}
