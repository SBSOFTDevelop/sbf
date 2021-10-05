package ru.sbsoft.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FormProcessorInfo {

    private String formType;
    private String formRights;
    private String className;
    private List<LookupFieldInfo> fields;

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getFormRights() {
        return formRights;
    }

    public void setFormRights(String formRights) {
        this.formRights = formRights;
    }

    public List<LookupFieldInfo> getFields() {
        if (null == fields) {
            fields = new ArrayList<>();
        }
        return fields;
    }

    public boolean addField(final LookupFieldInfo e) {
        return getFields().add(e);
    }

    public boolean addFields(Collection<? extends LookupFieldInfo> c) {
        return getFields().addAll(c);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

}
