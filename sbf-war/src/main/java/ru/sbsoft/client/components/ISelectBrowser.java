package ru.sbsoft.client.components;

import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.shared.model.LookupInfoModel;

/**
 * Браузер для испоьзования с {@link ru.sbsoft.client.components.form.LookupField}
 * @param <M> модель данных
 */
public interface ISelectBrowser<M extends LookupInfoModel> {

    void setOwnerLookupField(ILookupField<M> owner);

    void show();

    BaseGrid getGrid();
}
