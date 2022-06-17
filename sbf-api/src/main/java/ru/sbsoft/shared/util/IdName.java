package ru.sbsoft.shared.util;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author kiselev
 */
public class IdName<T> implements Serializable {

    private String name;
    private T id;
    
    public IdName(T id, String name) {
        this.name = name;
        this.id = id;
    }

    protected IdName() {
    }

    public String getName() {
        return name;
    }

    public T getId() {
        return id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IdName<?> other = (IdName<?>) obj;
        return Objects.equals(this.id, other.id);
    }
}
