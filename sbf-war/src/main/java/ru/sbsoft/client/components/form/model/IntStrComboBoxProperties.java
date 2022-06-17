package ru.sbsoft.client.components.form.model;

import ru.sbsoft.svc.data.shared.LabelProvider;
import ru.sbsoft.svc.data.shared.ModelKeyProvider;
import ru.sbsoft.svc.data.shared.PropertyAccess;

/**
 * Предназначен для создания объекта доступа к свойствам {@link ru.sbsoft.client.components.form.model.IntStrComboBoxModel} с помощью {@link com.google.gwt.core.client.GWT#create(java.lang.Class)}
 * Используется в {@link ru.sbsoft.client.components.form.IntStrComboBox}
 * @author Sokoloff
 */
public interface IntStrComboBoxProperties extends PropertyAccess<IntStrComboBoxModel> {

    ModelKeyProvider<IntStrComboBoxModel> id();

    LabelProvider<IntStrComboBoxModel> name();
}
