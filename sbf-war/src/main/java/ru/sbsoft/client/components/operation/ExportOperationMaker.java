package ru.sbsoft.client.components.operation;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.ContextGrid;
import ru.sbsoft.shared.CommonOperationEnum;

/**
 * Фабрика параметров операции по экспорту данных таблицы в виде файла электронной таблицы (xls).
 */
public class ExportOperationMaker extends GridOperationMaker {

	public ExportOperationMaker(ContextGrid grid) {
		super(CommonOperationEnum.EXPORT, grid);
	}

	@Override
	public AbstractOperation createOperation() {
		final GridOperation operation = GridOperation.create(getGrid(), getType(), new GridOperationParamForm(I18n.get(getType().getTitle()), getGrid()));
		operation.setReloadOnComplete(false);
		operation.setUnmarkOnComplete(false);
		return operation;
	}
}
