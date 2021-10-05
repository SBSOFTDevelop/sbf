package ru.sbsoft.dao.abstracts;

import javax.annotation.security.PermitAll;
import ru.sbsoft.dao.IApplicationDao;
import ru.sbsoft.model.IOperationSettings;
import ru.sbsoft.model.OperationSettings;

/**
 * Базовый абстрактный класс предоставляет метод, возвращающий
 * объект настроек, зависящих от операций.
 * @author rfa
 */
@PermitAll
public abstract class AbstractApplicationDaoBean implements IApplicationDao {

    private static final OperationSettings OPERATION_SETTINGS = new OperationSettings();

    @Override
    public IOperationSettings getOperationSettings() {
        return OPERATION_SETTINGS;
    }

}
