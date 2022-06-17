package ru.sbsoft.shared.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Базовая модель данных для браузера
 */
public class MarkModel implements Serializable {

    private BigDecimal RECORD_ID;

    public BigDecimal getRECORD_ID() {
        if (null == RECORD_ID) {
            throw new NullPointerException("В модели не инициализирован первичный ключ RECORD_ID.");
        }
        return RECORD_ID;
    }

    public void setRECORD_ID(BigDecimal RECORD_ID) {
        this.RECORD_ID = RECORD_ID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MarkModel other = (MarkModel) obj;
        return Objects.equals(this.RECORD_ID, other.RECORD_ID);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.RECORD_ID != null ? this.RECORD_ID.hashCode() : 0);
        return hash;
    }
}
