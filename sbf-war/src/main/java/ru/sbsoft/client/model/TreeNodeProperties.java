package ru.sbsoft.client.model;

import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.svc.data.shared.ModelKeyProvider;
import ru.sbsoft.svc.data.shared.PropertyAccess;
import ru.sbsoft.shared.TreeNode;

/**
 * Обеспечивает доступ к идентификатору и наименованию в модели узла дерева.
 */
public class TreeNodeProperties<T> implements PropertyAccess<TreeNode<T>> {

	public ModelKeyProvider<TreeNode<T>> key() {
		return new ModelKeyProvider<TreeNode<T>>() {
			@Override
			public String getKey(TreeNode<T> item) {
				if (null == item.getKey()) {
					return null;
				}
				return item.getKey().toString();
			}
		};
	}

	public ValueProvider<TreeNode<T>, String> title() {
		return new ValueProvider<TreeNode<T>, String>() {
			@Override
			public String getValue(TreeNode<T> item) {
				return item.getTitle();
			}

			@Override
			public void setValue(TreeNode<T> item, String value) {
				throw new UnsupportedOperationException("Tree is intended not editable directly. Use BaseForm mechanizm");
			}

			@Override
			public String getPath() {
				return "title";
			}
		};
	}
}
