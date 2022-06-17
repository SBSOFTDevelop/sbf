package ru.sbsoft.dao;

import ru.sbsoft.shared.interfaces.GridType;

/**
 *
 * @author Kiselev
 */
public interface ITemplateManager {

    AbstractTemplate initTemplate(GridType type) throws Exception;
}
