package ru.sbsoft.client.components.tree;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.TreeContext;
import ru.sbsoft.shared.TreeNode;

/**
 *
 * @author Kiselev
 */
public class SimpleTree<K extends Number> extends AbstractSimpleTree<K> {
    private final TreeContext context;
    private final String rootName;

    public SimpleTree(TreeContext context) {
        this(context, null);
    }
    
    public SimpleTree(TreeContext context, String rootName) {
        if(context == null){
            throw new IllegalArgumentException("TreeContext must be set");
        }
        this.context = context;
        this.rootName = rootName;
    }

    public TreeContext getContext() {
        return context;
    }

    @Override
    protected String getRootName(){
        return rootName;
    }

    @Override
    public void setFilter(List<FilterInfo> filters) {
        context.setParentFilters(filters);
        refresh(true);
    }

    @Override
    protected void loadTreeItems(TreeNode<K> parent, AsyncCallback<List<TreeNode<K>>> callback) {
        SBFConst.TREE_SERVICE.getTreeItems(getContext(), parent, callback);
    }
}
