package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.components.operation.IOperationMaker;

/**
 * Действие пункта меню, запускающее пользовательскую операцию.
 * @author balandin
 * @since Oct 7, 2013 6:16:31 PM
 */
public class GridOperationAction extends GridAction {

	private final IOperationMaker operationMaker;

	public GridOperationAction(BaseGrid grid, IOperationMaker operationMaker) {
		super(grid);
		this.operationMaker = operationMaker;
	}

	@Override
	protected void onExecute() {
		operationMaker.createOperation().startOperation();
	}
}
