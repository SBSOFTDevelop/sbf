package ru.sbsoft.client.components.form.model;

import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * Предназначен для создания объекта доступа к свойствам {@link ru.sbsoft.client.components.form.model.StrStrComboBoxModel} с помощью {@link com.google.gwt.core.client.GWT#create(java.lang.Class)}
 * Используется в {@link ru.sbsoft.client.components.form.StrStrComboBox}
 * @author Sokoloff
 */
public interface StrStrComboBoxProperties extends PropertyAccess<StrStrComboBoxModel> {

    ModelKeyProvider<StrStrComboBoxModel> id();

    LabelProvider<StrStrComboBoxModel> name();
}
