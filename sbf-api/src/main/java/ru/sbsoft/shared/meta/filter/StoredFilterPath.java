package ru.sbsoft.shared.meta.filter;

import ru.sbsoft.shared.param.DTO;

/**
 *
 * @author Kiselev
 */
public class StoredFilterPath implements DTO {
    private String identityName;
    private String filterName;

    private StoredFilterPath() {
    }
    
    public StoredFilterPath(String identityName, String filterName) {
        this.identityName = identityName;
        this.filterName = filterName;
    }

    public String getIdentityName() {
        return identityName;
    }

    public String getFilterName() {
        return filterName;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + hashCode(this.identityName);
        hash = 29 * hash + hashCode(this.filterName);
        return hash;
    }
    
    private static int hashCode(Object o){
        return o != null ? o.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StoredFilterPath other = (StoredFilterPath) obj;
        if (!equals(this.identityName, other.identityName)) {
            return false;
        }
        return equals(this.filterName, other.filterName);
    }

    private static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    @Override
    public String toString() {
        return "StoredFilterPath{" + "identityName=" + identityName + ", filterName=" + filterName + '}';
    }
    
    public boolean isDefault(){
        return isEmpty(identityName) && isEmpty(filterName);
    }
    
    private boolean isEmpty(String s){
        return s == null || s.trim().isEmpty();
    }
    
}
