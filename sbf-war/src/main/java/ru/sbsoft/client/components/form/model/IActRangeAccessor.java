package ru.sbsoft.client.components.form.model;

import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.svc.data.shared.PropertyAccess;
import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.shared.model.IActRangeModel;


public interface IActRangeAccessor<M extends IActRangeModel> extends PropertyAccess<M> {

    ValueProvider<IActRangeModel, YearMonthDay> begDate();

    ValueProvider<IActRangeModel, YearMonthDay> endDate();
}
