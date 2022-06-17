package ru.sbsoft.client.components;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import java.util.List;
import ru.sbsoft.shared.model.LookupInfoModel;

/**
 * Поле выбора значения (или нескольких) из списка (например, браузера) на форме. Основная реализация: {@link ru.sbsoft.client.components.form.LookupField}
 *
 * @param <M> модель данных
 */
public interface ILookupField<M extends LookupInfoModel> extends HasValueChangeHandlers<M> {

    void selectValue(Runnable callback);

    void setValue(M value, boolean fireEvents);

    void setValues(List<M> values);
    
    void setValues(List<M> values, boolean fireEvents);
}
