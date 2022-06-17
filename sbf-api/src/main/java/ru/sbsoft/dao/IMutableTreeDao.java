package ru.sbsoft.dao;

import java.util.List;
import ru.sbsoft.shared.TreeContext;
import ru.sbsoft.shared.TreeNode;

/**
 * Интерфейс для поддержки иерархической структуры (дерево) объектов-узлов,
 * экземпляров класса.
 */
public interface IMutableTreeDao<K extends Number> extends ITreeDao<K> {

    void setParent(TreeContext context, List<TreeNode<K>> nodes, TreeNode<K> parent);

    TreeNode<K> getTreeItem(TreeContext context, K key);
}
