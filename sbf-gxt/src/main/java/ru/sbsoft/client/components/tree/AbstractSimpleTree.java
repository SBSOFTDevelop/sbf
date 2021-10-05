package ru.sbsoft.client.components.tree;

import ru.sbsoft.shared.TreeNode;
import ru.sbsoft.shared.TreeNodeModel;

/**
 *
 * @author Kiselev
 */
public abstract class AbstractSimpleTree<K extends Number> extends AbstractTree<K, TreeNode<K>> {

    @Override
    protected TreeNode<K> createRootNode() {
        return getRootName() != null ? new TreeNodeModel<K>(null, getRootName(), false) : null;
    }

    protected abstract String getRootName();
}
