package ru.sbsoft.shared.tree;

import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.TreeNode;

/**
 *
 * @author Kiselev
 */
public interface ITreeParentFilterFactory {

    FilterInfo<?> createFilter(String filterName, TreeNode<?> node);
}
