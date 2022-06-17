package ru.sbsoft.client.components.tree;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.math.BigDecimal;
import ru.sbsoft.client.components.form.*;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.sbf.app.model.FormModel;
import ru.sbsoft.shared.TreeNode;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public interface ITreeFormFactory<M extends FormModel, K, T extends TreeNode<K>> {

    void createEditForm(T node, T parentNode, AsyncCallback<BaseForm<M>> callback);

    FormContext getFormContext(T node, T parentNode);
    
    BigDecimal toFormModelId(T node);
    
    K toNodeId(FormModel model);

}
