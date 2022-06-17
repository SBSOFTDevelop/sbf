package ru.sbsoft.client.components.tree;

import ru.sbsoft.client.components.IValSelectHandler;
import com.google.gwt.core.client.Callback;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.sbsoft.svc.data.shared.TreeStore;
import ru.sbsoft.svc.data.shared.loader.BeforeLoadEvent;
import ru.sbsoft.svc.data.shared.loader.LoadEvent;
import ru.sbsoft.svc.data.shared.loader.LoadExceptionEvent;
import ru.sbsoft.svc.data.shared.loader.LoaderHandler;
import ru.sbsoft.svc.widget.core.client.tree.Tree;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.components.tree.AbstractTree;
import ru.sbsoft.client.components.tree.SimpleTree;
import ru.sbsoft.client.model.TreeNodeProperties;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.TreeContext;
import ru.sbsoft.shared.TreeNode;
import ru.sbsoft.shared.interfaces.TreeType;

/**
 *
 * @author Kiselev
 */
public class TreeSelector<T extends Number> implements IValSelectHandler<TreeNode<T>> {

    private final AbstractTree<T, TreeNode<T>> tree;
    private final TreeSelectDialog dlg;
    private TreeShowMode showMode = TreeShowMode.FULL;

    public TreeSelector(TreeType treeType) {
        this(treeType, null);
    }

    public TreeSelector(TreeType treeType, final TreeNodeProperties<T> nodeProperties) {
        this(nodeProperties == null ? new SimpleTree<T>(new TreeContext(treeType)) : new SimpleTree<T>(new TreeContext(treeType)) {
            @Override
            public TreeNodeProperties getNodeProps() {
                return nodeProperties;
            }
        });
    }

    public TreeSelector(AbstractTree<T, TreeNode<T>> tree) {
        this.tree = tree;
        dlg = new TreeSelectDialog(tree);
    }

    public TreeSelector<T> setTitle(String title) {
        if (dlg != null) {
            dlg.setHeading(title);
        }
        return this;
    }

    public Tree<TreeNode<T>, String> getTree() {
        return tree.getTree();
    }

    public TreeSelector<T> setSelectLeafOnly(boolean b) {
        dlg.setSelectLeafOnly(b);
        return this;
    }

    public TreeSelector<T> setShowMode(TreeShowMode showMode) {
        this.showMode = showMode;
        return this;
    }

    @Override
    public String toString(TreeNode<T> key) {
        if (showMode == TreeShowMode.SHORT) {
            return key != null ? key.getTitle() : "null";
        }
        final String div = "/";
        StringBuilder buf = new StringBuilder();
        TreeNode<T> node = dlg.findNode(key);
        if (node != null) {
            List<String> l = new ArrayList<String>();
            TreeStore<TreeNode<T>> store = tree.getTree().getStore();
            while (node != null) {
                TreeNode<T> parent = store.getParent(node);
                if (showMode == TreeShowMode.WITHOUT_HEAD && parent == null) {
                    break;
                }
                l.add(node.getTitle());
                node = parent;
            }
            if (!l.isEmpty()) {
                int lastIndex = l.size() - 1;
                buf.append(l.get(lastIndex));
                for (int i = lastIndex - 1; i >= 0; i--) {
                    buf.append(div).append(l.get(i));
                }
            }
        } else if (key != null) {
            buf.append(key.getTitle());
        }
        return buf.toString();
    }

    @Override
    public void setFilter(List<FilterInfo> filters, final Callback<Void, Throwable> callback) {
        if (callback != null) {
            FilterLoaderHandler filterLoaderHandler = new FilterLoaderHandler(callback);
            HandlerRegistration reg = tree.getLoader().addLoaderHandler(filterLoaderHandler);
            filterLoaderHandler.setHandlerRegistration(reg);
            filterLoaderHandler.tryHandlerRemove();
        }
        tree.setFilter(filters);
    }

    @Override
    public boolean isValExists(TreeNode<T> val) {
        return dlg.findNode(val) != null;
    }

    @Override
    public void selectVal(TreeNode<T> initVal, AsyncCallback<TreeNode<T>> callback) {
        dlg.selectVal(callback);
    }

    private class FilterLoaderHandler implements LoaderHandler<TreeNode<T>, List<TreeNode<T>>> {

        private boolean done = false;
        private final Callback<Void, Throwable> callback;
        private HandlerRegistration reg = null;

        public FilterLoaderHandler(Callback<Void, Throwable> callback) {
            this.callback = callback;
        }

        @Override
        public void onBeforeLoad(BeforeLoadEvent<TreeNode<T>> event) {
        }

        @Override
        public void onLoadException(LoadExceptionEvent<TreeNode<T>> event) {
            callback.onFailure(event.getException());
            done = true;
            tryHandlerRemove();
        }

        @Override
        public void onLoad(LoadEvent<TreeNode<T>, List<TreeNode<T>>> event) {
            callback.onSuccess(null);
            done = true;
            tryHandlerRemove();
        }

        public void setHandlerRegistration(HandlerRegistration reg) {
            this.reg = reg;
        }

        public void tryHandlerRemove() {
            if (done && reg != null) {
                reg.removeHandler();
                reg = null;
            }
        }
    }

}
