package ru.sbsoft.dao.entity;

import java.math.BigDecimal;

/**
 *
 * @author Kiselev
 */
public interface IFormEntity extends IBaseEntity {

    @Override
    default Object getIdValue() {
        return getId();
    }

    BigDecimal getId();

    void setId(BigDecimal id);
}
