package ru.sbsoft.shared.model.user;

import ru.sbsoft.shared.param.DTO;

import java.util.Objects;

/**
 *
 * @author Kiselev
 */
public class Group implements DTO {

    private String code;
    private String name;

    public Group() {
    }
    
    public Group(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.code != null ? this.code.hashCode() : 0);
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
        final Group other = (Group) obj;
        return Objects.equals(this.code, other.code);
    }

}
