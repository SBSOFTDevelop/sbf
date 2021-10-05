package ru.sbsoft.dao;

import ru.sbsoft.shared.interfaces.GridType;

/**
 *
 * @author Kiselev
 */
public interface ITemplateManager {

    public AbstractTemplate initTemplate(GridType type) throws Exception;
}
