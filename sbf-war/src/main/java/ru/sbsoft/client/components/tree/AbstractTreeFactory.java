package ru.sbsoft.client.components.tree;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import ru.sbsoft.client.components.HasParentFilters;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.TreeContext;
import ru.sbsoft.shared.TreeNode;
import ru.sbsoft.shared.consts.Dict;
import ru.sbsoft.shared.interfaces.TreeType;
import ru.sbsoft.shared.tree.ITreeParentFilterFactory;
import ru.sbsoft.shared.tree.TreeKeyFilterFactory;

/**
 *
 * @author Kiselev
 */
public abstract class AbstractTreeFactory<K extends Number, T extends AbstractTree<K, TreeNode<K>>> {

    private final TreeType treeType;
    private String rootName = null;
    private ITreeParentFilterFactory parentFilterFactory = new TreeKeyFilterFactory();
    private String parentFilterName = Dict.FILTER_PARENT_ID;

    public AbstractTreeFactory(TreeType treeType) {
        this.treeType = treeType;
    }

    public AbstractTreeFactory<K, T> setRootName(String rootName) {
        this.rootName = rootName;
        return this;
    }

    public AbstractTreeFactory<K, T> setParentFilterFactory(ITreeParentFilterFactory parentFilterFactory) {
        this.parentFilterFactory = parentFilterFactory;
        return this;
    }

    public AbstractTreeFactory<K, T> setParentFilterName(String parentFilterName) {
        this.parentFilterName = parentFilterName != null ? parentFilterName : Dict.FILTER_PARENT_ID;
        return this;
    }

    protected TreeType getTreeType() {
        return treeType;
    }

    protected TreeContext getTreeContext() {
        return new TreeContext(treeType);
    }

    protected String getRootName() {
        return rootName;
    }

    protected ITreeParentFilterFactory getParentFilterFactory() {
        return parentFilterFactory;
    }

    protected String getParentFilterName() {
        return parentFilterName;
    }

    protected abstract T createTreeInstance();

    public T createTree() {
        return createTreeInstance();
    }

    public T createTree(HasParentFilters... grids) {
        return createTree(grids == null ? Collections.emptyList() : new ArrayList<>(Arrays.asList(grids)));
    }

    public T createTree(final List<? extends HasParentFilters> grids) {
        final T baseTree = createTreeInstance();
        if (grids != null && !grids.isEmpty()) {
            baseTree.addSelectionHandler(new SelectionHandler<TreeNode<K>>() {
                @Override
                public void onSelection(SelectionEvent<TreeNode<K>> event) {
                    final TreeNode<?> item = event.getSelectedItem();
                    List<FilterInfo> filters = new ArrayList<>();
                    if (null != item) {
                        filters.add(parentFilterFactory.createFilter(getParentFilterName(), item));
                    }
                    grids.forEach(grid -> {
                        if (grid != null) {
                            grid.setParentFilters(filters);
                        }
                    });
                }
            });
        }
        return baseTree;
    }
}
