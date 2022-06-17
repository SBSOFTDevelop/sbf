package ru.sbsoft.shared.interfaces;

import ru.sbsoft.shared.param.DTO;

/**
 * Базовый интерфейс типов сущностей, требующих идентификатора.
 * @see GridType
 * @see FormType
 * @author balandin
 * @since May 24, 2013 3:29:23 PM
 */
public interface ObjectType extends DTO {

    String getCode();
}
