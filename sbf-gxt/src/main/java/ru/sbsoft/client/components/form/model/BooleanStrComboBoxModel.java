package ru.sbsoft.client.components.form.model;

import java.io.Serializable;

public class BooleanStrComboBoxModel implements Serializable {

    private Boolean id;
    private String name;

    public BooleanStrComboBoxModel() {
    }

    public BooleanStrComboBoxModel(Boolean id, String name) {
        this.id = id;
        this.name = name;
    }

    public Boolean getId() {
        return id;
    }

    public void setId(Boolean id) {
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
        int hash = 7;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
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
        final BooleanStrComboBoxModel other = (BooleanStrComboBoxModel) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
