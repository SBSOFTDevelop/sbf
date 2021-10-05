package ru.sbsoft.client.components.operation;

import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.shared.interfaces.OperationType;

/**
 * Заготовка фабрики для {@link GridOperation}.
 * Наследники создают операцию в {@link OperationMaker#createOperation()} с помощью {@link GridOperation#create(ru.sbsoft.client.components.grid.BaseGrid, ru.sbsoft.shared.interfaces.OperationType, ru.sbsoft.client.components.operation.BaseOperationParamForm)}.
 * @author balandin
 * @since Oct 7, 2013 6:20:51 PM
 */
public abstract class GridOperationMaker extends OperationMaker {

	private final BaseGrid grid;

	public GridOperationMaker(OperationType type, BaseGrid grid) {
		super(type);
		this.grid = grid;
	}

	public BaseGrid getGrid() {
		return grid;
	}
}
