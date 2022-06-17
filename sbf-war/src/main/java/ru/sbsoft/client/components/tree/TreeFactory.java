package ru.sbsoft.client.components.tree;

import ru.sbsoft.shared.interfaces.TreeType;

/**
 *
 * @author Kiselev
 */
public class TreeFactory<K extends Number> extends AbstractTreeFactory<K, SimpleTree<K>> {

    public TreeFactory(TreeType treeType) {
        super(treeType);
    }

    @Override
    protected SimpleTree<K> createTreeInstance() {
        return new SimpleTree(getTreeContext(), getRootName());
    }
    
}
