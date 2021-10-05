package ru.sbsoft.dao.entity;

import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author Kiselev
 * @param <M>
 * @param <E>
 */
public interface IEntityConverter<M extends IFormModel, E extends IBaseEntity> {

    M toModel(E e);

    M toModel(E e, M modelToFill);
}
