package ru.sbsoft.shared;

import ru.sbsoft.shared.model.enums.YesNoEnum;

/**
 * Используется для создания {@link ru.sbsoft.svc.data.shared.ModelKeyProvider} и {@link ru.sbsoft.svc.data.shared.LabelProvider}
 * @see YesNoEnum
 * @see ru.sbsoft.client.components.form.ComboBoxUtils
 */
public interface IComboBoxModel {

    String getKey();

    String getLabel();
}
