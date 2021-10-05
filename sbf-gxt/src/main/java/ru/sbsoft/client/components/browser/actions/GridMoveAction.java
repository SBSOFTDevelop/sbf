package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.components.grid.BaseGrid;

/**
 * Базовый класс для всех действий по перемещению выделения строки в таблице.
 * @author balandin
 * @since Oct 3, 2013 4:41:14 PM
 */
public abstract class GridMoveAction extends GridAction {

    public GridMoveAction(BaseGrid grid) {
        super(grid);
    }
}
