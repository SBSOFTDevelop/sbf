package ru.sbsoft.client.components.tree;

import com.sencha.gxt.core.client.ValueProvider;
import ru.sbsoft.client.components.form.model.IFormModelAccessor;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.shared.model.TreeNodeSimpleFormModel;

public interface TreeNodeSimpleAccessor<M extends TreeNodeSimpleFormModel> extends IFormModelAccessor<M> {

    ValueProvider<TreeNodeSimpleFormModel, LookupInfoModel> parentNode();

    ValueProvider<TreeNodeSimpleFormModel, String> nodeName();

}
