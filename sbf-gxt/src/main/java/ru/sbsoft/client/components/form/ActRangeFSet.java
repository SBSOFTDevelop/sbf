package ru.sbsoft.client.components.form;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.handler.form.YearMonthDayHandler;
import ru.sbsoft.client.components.form.handler.form.builder.DualField;
import ru.sbsoft.client.components.form.handler.form.builder.FSet;
import ru.sbsoft.client.components.form.model.IActRangeAccessor;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;
import ru.sbsoft.shared.model.IActRangeModel;

/**
 *
 * @author Kiselev
 */
public class ActRangeFSet<M extends IFormModel & IActRangeModel> extends FSet<M> {

    protected final YearMonthDayHandler<M> begDateHandler;
    protected final YearMonthDayHandler<M> endDateHandler;

    public ActRangeFSet(IActRangeAccessor<M> a) {
        super(I18n.get(SBFEditorStr.labelActRange));
        super.add(new DualField<>(I18n.get(SBFEditorStr.labelActRangeStartEnd),
                begDateHandler = new YearMonthDayHandler<M>("", a.begDate()).setReq().setToolTip(I18n.get(SBFEditorStr.hintActRangeStart)),
                endDateHandler = new YearMonthDayHandler<M>("", a.endDate()).setToolTip(I18n.get(SBFEditorStr.hintActRangeEnd))));
    }

    public YearMonthDayHandler<M> getBegDateHandler() {
        return begDateHandler;
    }

    public YearMonthDayHandler<M> getEndDateHandler() {
        return endDateHandler;
    }
}
