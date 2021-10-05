package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import ru.sbsoft.shared.TreeContext;
import ru.sbsoft.shared.TreeNode;

/**
 * @see ITreeService
 */
@RemoteServiceRelativePath(ServiceConst.MUTABLE_TREE_SERVICE_SHORT)
public interface IMutableTreeService extends ITreeServiceBase {

    <K extends Number> void setParent(TreeContext context, List<TreeNode<K>> nodes, TreeNode<K> parent);

    <K extends Number> TreeNode<K> getTreeItem(TreeContext context, K key);
}
