package ru.sbsoft.generator;

import java.util.Objects;

/**
 * Описывает инъекцию (@EJB, @PersistenceContext, @Resource) в
 * CommonFormDaoBean.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class InjectionInfo implements Comparable<InjectionInfo> {

    private String className;
    private String varName;
    private String annotation;
    private String resourceName;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String attributeName) {
        this.varName = attributeName;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.className);
        hash = 23 * hash + Objects.hashCode(this.varName);
        hash = 23 * hash + Objects.hashCode(this.annotation);
        hash = 23 * hash + Objects.hashCode(this.resourceName);
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
        final InjectionInfo other = (InjectionInfo) obj;
        if (!Objects.equals(this.className, other.className)) {
            return false;
        }
        if (!Objects.equals(this.varName, other.varName)) {
            return false;
        }
        if (!Objects.equals(this.annotation, other.annotation)) {
            return false;
        }

        if (!Objects.equals(this.resourceName, other.resourceName)) {
            return false;
        }

        return true;
    }

    @Override
    public int compareTo(InjectionInfo o) {
        int result = this.className.compareTo(o.className);
        if (0 == result) {
            result = this.varName.compareTo(o.varName);
        }
        if (0 == result) {
            result = this.annotation.compareTo(o.annotation);
        }

        if (0 == result) {

            if (this.resourceName != null) {
                result = this.resourceName.compareTo(o.resourceName);
            } else if (o.resourceName != null) {
                result = o.resourceName.compareTo(this.resourceName);

            }

        }

        return result;
    }
}
