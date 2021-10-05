package ru.sbsoft.tree;

import java.util.List;
import ru.sbsoft.shared.TreeContext;
import ru.sbsoft.shared.TreeNode;

/**
 *
 * @author Kiselev
 */
public interface ITreeProvider<K extends Number> {

    List<TreeNode<K>> getTreeItems(TreeNode<K> parentKey, TreeContext treeContext);
}
