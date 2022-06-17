package ru.sbsoft.client.components.form.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Модель данных для {@link ru.sbsoft.client.components.form.StrStrComboBox}
 * @author panarin
 */
public class StrStrComboBoxModel implements Serializable {

    private String id;
    private String name;

    public StrStrComboBoxModel() {
    }

    public StrStrComboBoxModel(String value) {
        this.id = value;
        this.name = value;
    }

    public StrStrComboBoxModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StrStrComboBoxModel other = (StrStrComboBoxModel) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
