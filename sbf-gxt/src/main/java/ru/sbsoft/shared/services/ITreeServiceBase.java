package ru.sbsoft.shared.services;

import java.util.List;
import ru.sbsoft.shared.TreeContext;
import ru.sbsoft.shared.TreeNode;

/**
 * Возвращает иерархическую структуру оъектов заданного типа.
 *
 * @see TreeContext#getTreeType()
 */
public interface ITreeServiceBase extends SBFRemoteService {

    <K extends Number> List<TreeNode<K>> getTreeItems(TreeContext context, TreeNode<K> parent);
}
