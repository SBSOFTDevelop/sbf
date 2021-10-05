package ru.sbsoft.client.components.tree;

import ru.sbsoft.shared.TreeNode;

/**
 *
 * @author Kiselev
 */
public interface FormSaveHandler<M extends TreeNode<?>> {
    void saveModel(M m, TreeNode<?> parent);
}
