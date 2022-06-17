package ru.sbsoft.shared;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import ru.sbsoft.shared.interfaces.TreeType;

/**
 * Класс контекст иерархичесих данных организованных ввиде дерева.
 * <p>
 * Передается в конструторе {@link ru.sbsoft.shared.TreeContext.BaseTree#BaseTree(final TreeContext context)}
 * <p>
 * Например:
 * <pre>
 * final BaseTree baseTree = new BaseTree(new TreeContext(TreeEnum.CD_STAT_INSURER));
 * </pre>
 *
 * @author panarin
 */
public class TreeContext implements Serializable {

    private TreeType treeType;
    private List<FilterInfo> parentFilters = Collections.emptyList();

    public TreeContext() {
    }

    public TreeContext(TreeType treeType) {
        this.treeType = treeType;
    }

    public TreeType getTreeType() {
        return treeType;
    }

    public void setTreeType(TreeType treeType) {
        this.treeType = treeType;
    }

    public List<FilterInfo> getParentFilters() {
        return parentFilters;
    }

    public void setParentFilters(List<FilterInfo> parentFilters) {
        this.parentFilters = parentFilters != null ? parentFilters : Collections.<FilterInfo>emptyList();
    }
}
