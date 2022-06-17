package ru.sbsoft.sbf.app.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Базовый класс для обработки в форме: реализован доступ к ID.
 */
public class FormModel implements Serializable, IFormModel {

    public static final ModelValueProvider<BigDecimal, FormModel> ID_PROVIDER = new ModelValueProvider<BigDecimal, FormModel>() {
        @Override
        public BigDecimal getValue(FormModel model) {
            return model.getId();
        }

        @Override
        public void setValue(BigDecimal value, FormModel model) {
            model.setId(value);
        }
    };

    private BigDecimal id;

    public FormModel(FormModel m) {
        if (m != null) {
            id = m.getId();
        }
    }

    public FormModel() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final FormModel other = (FormModel) obj;
        return this.id == other.id || (this.id != null && this.id.equals(other.id));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public String getUiName() {
        return "";
    }
}
