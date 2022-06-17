package ru.sbsoft.client.components.form.model;

import ru.sbsoft.svc.data.shared.LabelProvider;
import ru.sbsoft.svc.data.shared.ModelKeyProvider;
import ru.sbsoft.svc.data.shared.PropertyAccess;

/**
 * Предназначен для создания объекта доступа к свойствам {@link ru.sbsoft.client.components.form.model.StrStrComboBoxModel} с помощью {@link com.google.gwt.core.client.GWT#create(java.lang.Class)}
 * Используется в {@link ru.sbsoft.client.components.form.StrStrComboBox}
 * @author Sokoloff
 */
public interface StrStrComboBoxProperties extends PropertyAccess<StrStrComboBoxModel> {

    ModelKeyProvider<StrStrComboBoxModel> id();

    LabelProvider<StrStrComboBoxModel> name();
}
