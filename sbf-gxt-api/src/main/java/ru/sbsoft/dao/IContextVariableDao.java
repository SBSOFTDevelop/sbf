package ru.sbsoft.dao;

import javax.ejb.Remote;
import ru.sbsoft.shared.model.ContextVariableModel;

/**
 * Возврщает/сохраняет переменные окружения (контекст) пользователя
 * Предполагает наличие реализации в виде EJB в каждом проекте
 */
@Remote
public interface IContextVariableDao {

    ContextVariableModel getContextVariable(String idVariable, String userName);

    void setContextVariable(ContextVariableModel contextVar);
}
