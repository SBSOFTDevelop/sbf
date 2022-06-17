package ru.sbsoft.client.components.form;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.math.BigDecimal;
import ru.sbsoft.client.components.tree.ITreeFormFactory;
import ru.sbsoft.sbf.app.model.FormModel;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.NamedFormType;
import ru.sbsoft.shared.TreeNode;

/**
 *
 * @author Kiselev
 */
public abstract class SimpleTreeFormFactory<M extends FormModel> extends GridFormFactory<M> implements ITreeFormFactory<M, BigDecimal, TreeNode<BigDecimal>>{

    public SimpleTreeFormFactory(NamedFormType formType) {
        super(formType);
    }

    @Override
    public void createEditForm(TreeNode<BigDecimal> node, TreeNode<BigDecimal> parentNode, AsyncCallback<BaseForm<M>> callback) {
        createEditForm(null, callback);
    }

    @Override
    public FormContext getFormContext(TreeNode<BigDecimal> node, TreeNode<BigDecimal> parentNode) {
        return getFormContext(null);
    }

    @Override
    public BigDecimal toFormModelId(TreeNode<BigDecimal> node) {
        return node != null ? node.getKey() : null;
    }

    @Override
    public BigDecimal toNodeId(FormModel model) {
        return model != null ? model.getId() : null;
    }
    
}
