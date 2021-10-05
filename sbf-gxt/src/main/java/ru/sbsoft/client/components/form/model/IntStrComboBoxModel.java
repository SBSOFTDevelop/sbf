package ru.sbsoft.client.components.form.model;

import java.io.Serializable;

/**
 * Модель данных для {@link ru.sbsoft.client.components.form.IntStrComboBox}
 * @author Sokoloff
 */
public class IntStrComboBoxModel implements Serializable {

    private Integer id;
    private String name;

    public IntStrComboBoxModel() {
    }

    public IntStrComboBoxModel(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IntStrComboBoxModel other = (IntStrComboBoxModel) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
