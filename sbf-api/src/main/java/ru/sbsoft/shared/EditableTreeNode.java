package ru.sbsoft.shared;

/**
 * Добавлен ключ родителя для обновления в дереве самого элемента
 * @author sokolov
 * @param <T>
 */
public interface EditableTreeNode<T> extends TreeNode<T> {
    
    T getParentKey();
    
}
