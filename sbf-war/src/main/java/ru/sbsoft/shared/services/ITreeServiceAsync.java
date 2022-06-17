package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import ru.sbsoft.shared.TreeContext;
import ru.sbsoft.shared.TreeNode;

/**
 * @see ITreeService
 */
public interface ITreeServiceAsync extends ISBFServiceAsync {

   <K extends Number> void getTreeItems(TreeContext context, TreeNode<K> parent, AsyncCallback<List<TreeNode<K>>> callback);
}
