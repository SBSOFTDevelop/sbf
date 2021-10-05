package ru.sbsoft.tree;

import java.util.List;
import ru.sbsoft.shared.TreeNode;

/**
 *
 * @author Kiselev
 */
public interface IMutableTreeProvider<K extends Number> extends ITreeProvider<K> {
    
    void setParent(List<TreeNode<K>> nodes, TreeNode<K> parent);

    TreeNode<K> getTreeItem(K key);
}
