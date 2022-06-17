package ru.sbsoft.client.components.operation.kladr;

import ru.sbsoft.client.components.operation.AbstractOperation;
import ru.sbsoft.client.components.operation.OperationMaker;
import ru.sbsoft.client.components.operation.TypedOperation;
import ru.sbsoft.shared.CommonOperationEnum;

/**
 * Создает стандартную операцию импорта КЛАДР. Для работы операции в серверном
 * модуле проекта должен быть создан класс вида: {@code
 * @SystemOperationProcessor(CommonOperationEnum.KLADR_IMPORT)
 * public class KLADRImportRunner extends AbstractKLADRImportRunner {
 * }
 * }
 * @see ru.sbsoft.operation.kladr.AbstractKLADRImportRunner
 * @author balandin
 * @since Mar 11, 2013 1:28:07 PM
 */
public class KLADRImportOperationMaker extends OperationMaker {

    public KLADRImportOperationMaker() {
        super(CommonOperationEnum.KLADR_IMPORT);
    }

    @Override
    public AbstractOperation createOperation() {
        final TypedOperation operation = TypedOperation.create(getType(), new KLADRImportParamForm(null, getOperationCode()));
        return operation;
    }
}
