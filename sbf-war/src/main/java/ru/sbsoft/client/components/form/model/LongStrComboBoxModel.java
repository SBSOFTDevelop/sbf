package ru.sbsoft.client.components.form.model;

import java.io.Serializable;
import java.util.Objects;

public class LongStrComboBoxModel implements Serializable {

    private Long id;
    private String name;

    public LongStrComboBoxModel() {
    }

    public LongStrComboBoxModel(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        int hash = 5;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
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
        final LongStrComboBoxModel other = (LongStrComboBoxModel) obj;
        return Objects.equals(this.id, other.id);
    }
}
