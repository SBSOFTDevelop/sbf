package ru.sbsoft.client.components.form.model;

import ru.sbsoft.svc.data.shared.LabelProvider;
import ru.sbsoft.svc.data.shared.ModelKeyProvider;
import ru.sbsoft.svc.data.shared.PropertyAccess;

public interface BooleanStrComboBoxProperties extends PropertyAccess<BooleanStrComboBoxModel> {

    ModelKeyProvider<BooleanStrComboBoxModel> id();

    LabelProvider<BooleanStrComboBoxModel> name();
}
