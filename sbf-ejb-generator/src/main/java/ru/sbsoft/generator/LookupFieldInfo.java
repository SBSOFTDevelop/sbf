package ru.sbsoft.generator;

import java.util.Objects;

/**
 * Описывает поле в классе Processor, помеченное как @Lookup.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class LookupFieldInfo implements Comparable<LookupFieldInfo> {

    /**
     * Название поля.
     */
    private String fieldName;
    /**
     * Название перменной DaoBean'а, из которой будет "проброшена" инъекция.
     */
    private String varName;
    /**
     * Название класса, которому принадлежит поле. Может различаться, например, если 
     * одинаковые поля есть в классе и в его родителе.
     */
    private String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.fieldName);
        hash = 37 * hash + Objects.hashCode(this.varName);
        hash = 37 * hash + Objects.hashCode(this.className);
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
        final LookupFieldInfo other = (LookupFieldInfo) obj;
        if (!Objects.equals(this.fieldName, other.fieldName)) {
            return false;
        }
        if (!Objects.equals(this.varName, other.varName)) {
            return false;
        }
        return Objects.equals(this.className, other.className);
    }

    @Override
    public int compareTo(LookupFieldInfo o) {
        int result = this.fieldName.compareTo(o.fieldName);
        if (0 == result) {
            result = this.varName.compareTo(o.varName);
        }
        if (0 == result) {
            result = this.className.compareTo(o.className);
        }
        return result;
    }

}
