package ru.sbsoft.client.components.form.model;

import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface BooleanStrComboBoxProperties extends PropertyAccess<BooleanStrComboBoxModel> {

    ModelKeyProvider<BooleanStrComboBoxModel> id();

    LabelProvider<BooleanStrComboBoxModel> name();
}
