package ru.sbsoft.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProcessorInfo implements Comparable<ProcessorInfo> {

    private String code;
    private String rights;
    private String varName;
    private String className;
    private String postConstructMethod;
    private String createMethodName;
    private String title;
    private List<LookupFieldInfo> fields;
    private boolean transaction;
    private String unitName;

    public boolean isTransaction() {
        return transaction;
    }

    public void setTransaction(boolean transaction) {
        this.transaction = transaction;
    }

    
    
    
    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getPostConstructMethod() {
        return postConstructMethod;
    }

    public void setPostConstructMethod(String postConstructMethod) {
        this.postConstructMethod = postConstructMethod;
    }

    public String getCreateMethodName() {
        return createMethodName;
    }

    public void setCreateMethodName(String createMethodName) {
        this.createMethodName = createMethodName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int compareTo(ProcessorInfo o) {
        return code.compareTo(o.code);
    }

}
