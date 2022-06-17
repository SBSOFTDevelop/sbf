package ru.sbsoft.client.components.tree;

import com.google.gwt.event.logical.shared.SelectionHandler;
import ru.sbsoft.svc.data.shared.TreeStore;
import ru.sbsoft.svc.widget.core.client.tree.Tree;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.shared.TreeNodeModel;

/**
 * Предоставляет дополнительные сервисные функции, ориентированные на работу с деревом.
 * @param <T> модель данных узла дерева
 */
public abstract class TreeSelectionHandler<T> implements SelectionHandler<T> {

	protected static boolean isRoot(Tree tree, TreeNodeModel item) {
		final List rootItems = tree.getStore().getRootItems();
		return rootItems != null && rootItems.size() == 1 && rootItems.get(0) == item;
	}

	protected List<String> getAllKeysFromSelected(Tree tree, TreeNodeModel item) {
		List<String> result = new ArrayList<String>();
		scan(tree.getStore(), item, result);
		return result;
	}

	protected static List<String> getChildOnlyKeysFromSelected(Tree tree, TreeNodeModel item) {
		List<String> result = new ArrayList<String>();
		TreeStore<TreeNodeModel> store = tree.getStore();
		for (TreeNodeModel child : store.getChildren(item)) {
			result.add(String.valueOf(child.getKey()));
		}
		return result;
	}

	protected static void scan(TreeStore<TreeNodeModel> store, TreeNodeModel item, List<String> result) {
		result.add(String.valueOf(item.getKey()));
		if (store.getChildCount(item) > 0) {
			for (TreeNodeModel child : store.getChildren(item)) {
				scan(store, child, result);
			}
		}
	}
}
