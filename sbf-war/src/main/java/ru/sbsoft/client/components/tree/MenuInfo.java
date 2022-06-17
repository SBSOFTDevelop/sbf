package ru.sbsoft.client.components.tree;

/**
 * Определяет размер и расположение главного меню приложения.
 * Используется внутри {@link ru.sbsoft.client.components.treemenu.ApplicationContainer}
 * @author balandin
 * @since Apr 12, 2013 8:17:30 PM
 */
public interface MenuInfo {

    int getWidth();

    void setWidth(int width);

    int getPosition();

    void setPosition(int position);
}