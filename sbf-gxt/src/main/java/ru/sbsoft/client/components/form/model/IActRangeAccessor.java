package ru.sbsoft.client.components.form.model;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.shared.model.IActRangeModel;


public interface IActRangeAccessor<M extends IActRangeModel> extends PropertyAccess<M> {

    ValueProvider<IActRangeModel, YearMonthDay> begDate();

    ValueProvider<IActRangeModel, YearMonthDay> endDate();
}
