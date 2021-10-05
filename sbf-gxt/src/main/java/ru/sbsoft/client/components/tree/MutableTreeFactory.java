package ru.sbsoft.client.components.tree;

import ru.sbsoft.shared.NamedFormType;
import ru.sbsoft.shared.interfaces.TreeType;

/**
 *
 * @author Kiselev
 */
public class MutableTreeFactory<K extends Number> extends AbstractTreeFactory<K, GenericTree<K>> {

    private ITreeFormFactory formFactory = null;

    public MutableTreeFactory(TreeType treeType) {
        super(treeType);
    }

    public MutableTreeFactory<K> setFormFactory(ITreeFormFactory formFactory) {
        this.formFactory = formFactory;
        return this;
    }

    public MutableTreeFactory<K> setFormFactory(NamedFormType formType) {
        return setFormFactory(new TreeNodeSimpleForm(formType));
    }

    @Override
    protected GenericTree<K> createTreeInstance() {
        GenericTree<K> t = new GenericTree<K>(getTreeContext(), getRootName(), formFactory);
        return t;
    }

}
