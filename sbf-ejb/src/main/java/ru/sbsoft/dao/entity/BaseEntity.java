package ru.sbsoft.dao.entity;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

/**
 * Базовый абстрактный класс для всех сущностей
 * {@link javax.persistence.Entity}, <br> или классов отражений
 * {@link javax.persistence.MappedSuperclass}, имеющих системные
 * (технологические) поля <i>(например: UPDATE_USER,UPDATE_DATE, и т.п.)</i>.
 *
 * @author balandin
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable, IBaseEntity {

    /*
    Реализации методов класса Object на основе ID
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getIdValue());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !getClass().equals(object.getClass())) {
            return false;
        }
        return getIdValue() != null ? Objects.equals(this.getIdValue(), ((IBaseEntity) object).getIdValue()) : false;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(getClass().getSimpleName()).append("[id=").append(getIdValue()).append(']').toString();
    }
}
