package ru.sbsoft.dao;

import java.util.List;
import ru.sbsoft.shared.TreeContext;
import ru.sbsoft.shared.TreeNode;

/**
 * Интерфейс для поддержки иерархической структуры (дерево) объектов-узлов,
 * экземпляров класса {@link ru.sbsoft.shared.BdTreeNodeModel}.
 */
public interface ITreeDao<K extends Number> {

    List<TreeNode<K>> getTreeItems(TreeContext context, TreeNode<K> parent);
}
