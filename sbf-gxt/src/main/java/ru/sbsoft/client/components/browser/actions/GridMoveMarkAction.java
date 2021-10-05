package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.components.grid.BaseGrid;

/**
 * Перемещение выделения по строкам таблицы, имеющим отметки.
 * @author balandin
 * @since Oct 7, 2013 4:29:03 PM
 */
public abstract class GridMoveMarkAction extends GridAction {

	public GridMoveMarkAction(BaseGrid grid) {
		super(grid);
	}

	@Override
	public boolean checkEnabled() {
		return super.checkEnabled() && getGrid().isMarksAllowed() && getGrid().hasMarkedRecords();
	}
}
