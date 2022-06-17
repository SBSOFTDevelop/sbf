package ru.sbsoft.shared.tree;

import java.math.BigDecimal;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.TreeNode;
import ru.sbsoft.shared.filter.KeyFilterInfo;

/**
 *
 * @author Kiselev
 */
public class TreeKeyFilterFactory implements ITreeParentFilterFactory {

    @Override
    public FilterInfo<?> createFilter(String filterName, TreeNode<?> node) {
        Object key = node != null ? node.getKey() : null;
        BigDecimal numKey = key instanceof BigDecimal ? (BigDecimal)key : (key instanceof Number) ? new BigDecimal((key.toString())) : KeyFilterInfo.FILTER_NOTEXIST_VALUE;
        return new KeyFilterInfo(filterName, numKey);
    }
}
