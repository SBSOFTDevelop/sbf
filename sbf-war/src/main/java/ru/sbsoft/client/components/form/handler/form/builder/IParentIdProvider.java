package ru.sbsoft.client.components.form.handler.form.builder;

import java.math.BigDecimal;
import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author Kiselev
 * @param <DataModel>
 */
public interface IParentIdProvider<DataModel extends IFormModel> {

    BigDecimal getParentId(DataModel m);
}
