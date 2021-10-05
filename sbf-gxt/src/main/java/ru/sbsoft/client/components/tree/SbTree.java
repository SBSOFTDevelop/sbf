package ru.sbsoft.client.components.tree;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.tree.Tree;

/**
 * Компонент перепределяет логику определения листьев дерева: не по осутствию
 * потомков, а по признаку isLeaf в модели, который определяется
 * бизнес-правилами.
 *
 * @author Sokoloff
 */
public class SbTree<M extends ru.sbsoft.shared.TreeNode, C> extends Tree<M, C> {

    public SbTree(TreeStore<M> store, ValueProvider<? super M, C> valueProvider) {
        super(store, valueProvider);
    }

    public SbTree(TreeStore<M> store, ValueProvider<? super M, C> valueProvider, TreeAppearance appearance) {
        super(store, valueProvider, appearance);
    }

    @Override
    public boolean isLeaf(M model) {
        return model.isLeaf();
    }

}
