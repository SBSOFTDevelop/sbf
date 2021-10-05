package ru.sbsoft.shared;

import ru.sbsoft.shared.model.enums.YesNoEnum;

/**
 * Используется для создания {@link com.sencha.gxt.data.shared.ModelKeyProvider} и {@link com.sencha.gxt.data.shared.LabelProvider}
 * @see YesNoEnum
 * @see ru.sbsoft.client.components.form.ComboBoxUtils
 */
public interface IComboBoxModel {

    public String getKey();

    public String getLabel();
}
