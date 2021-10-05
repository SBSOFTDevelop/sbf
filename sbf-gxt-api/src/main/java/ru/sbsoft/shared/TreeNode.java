package ru.sbsoft.shared;

import java.io.Serializable;

/**
 * Элемент дерева для построения браузеров с навигацией в виде дерева
 * @see TreeFundModel
 * @see ru.sbsoft.client.components.tree.FundTreeBrowser
 * @author balandin
 * @since Apr 11, 2013 4:01:23 PM
 */
public interface TreeNode<T> extends Serializable {

    T getKey();
    
    String getTitle();

    boolean isLeaf();
}
