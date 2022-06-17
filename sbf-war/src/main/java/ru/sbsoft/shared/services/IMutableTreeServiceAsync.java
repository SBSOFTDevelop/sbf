package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import ru.sbsoft.shared.TreeContext;
import ru.sbsoft.shared.TreeNode;

/**
 * @see ITreeService
 */
public interface IMutableTreeServiceAsync extends ITreeServiceAsync {

    <K extends Number> void setParent(TreeContext context, List<TreeNode<K>> nodes, TreeNode<K> parent, AsyncCallback<Void> callback);

    <K extends Number> void getTreeItem(TreeContext context, K key, AsyncCallback<TreeNode<K>> callback);
}
