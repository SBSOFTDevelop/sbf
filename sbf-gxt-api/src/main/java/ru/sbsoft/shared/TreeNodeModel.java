package ru.sbsoft.shared;

import java.util.Objects;

/**
 * Узел дерева с полем типа Long наследник от обобщенного класса {@link TreeNode}.
 * @author balandin
 * @since Apr 11, 2013 3:58:13 PM
 */
public class TreeNodeModel<K> implements TreeNode<K> {

    private K key;
    private String title;
    private boolean leaf;

    public TreeNodeModel() {
    }

    public TreeNodeModel(K key, String title, boolean leaf) {
        this.key = key;
        this.title = title;
        this.leaf = leaf;
    }

    @Override
    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public boolean isLeaf() {
        return leaf;
    }

    @Override
    public String toString() {
        return key + " / " + title;
    }

    @Override
    public int hashCode() {
        return 55 + Objects.hashCode(this.key);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj != null && getClass() == obj.getClass() && Objects.equals(this.key, ((TreeNodeModel<?>) obj).key));
    }
}
