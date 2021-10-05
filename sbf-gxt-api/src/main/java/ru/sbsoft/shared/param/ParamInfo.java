package ru.sbsoft.shared.param;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import ru.sbsoft.shared.FilterInfo;

public abstract class ParamInfo<T> implements Serializable {

    public static final String START_DATE = "START_DATE";
    public static final String END_DATE = "END_DATE";
    public static final String FILE_FORMAT = "FILE_FORMAT";
    public static final String REPORT_ID = "__REPORT_ID";
    public static final String REPORT_FORMAT = "__REPORT_FORMAT";
    
    //
    private String name;
    private ParamTypeEnum type;
    private T value;

    public ParamInfo() {
    }

    public ParamInfo(final String name, final ParamTypeEnum type, final T value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }
    
    public static List<ParamInfo> asList(ParamInfo... paramInfos) {
        return Arrays.asList(paramInfos);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ParamTypeEnum getType() {
        return type;
    }

    public void setType(final ParamTypeEnum type) {
        this.type = type;
    }

    public T getValue() {
        return value;
    }

    public void setValue(final T value) {
        this.value = value;
    }

    protected static <V, P extends ParamInfo<V>> V getValue(List<ParamInfo> params, P param) {
        return getValue(params, (Class<P>) param.getClass(), param.getName());
    }
    
    public static <V, P extends ParamInfo<V>> V getValue(List<ParamInfo> params, Class<P> clazz, String name) {
        P p = getParam(params, clazz, name);
        return p != null ? (V) p.getValue() : null;
    }
    
    public static <V, P extends ParamInfo<V>> P getParam(List<ParamInfo> params, Class<P> clazz, String name) {
        P found = null;
        for (ParamInfo p : params) {
            if (p != null && Objects.equals(p.getName(), name)) {
                if (deepEq(clazz, p.getClass())) {
                    if (found != null) {
                        throw new IllegalArgumentException("Threre are many filters with name '" + name + "' and class '" + clazz.getName() + "'");
                    } else {
                        found = (P) p;
                    }
                }
            }
        }
        return found;
    }

    public static boolean deepEq(Class<?> c1, Class<?> c2) {
        while (c2 != null) {
            if (c2.equals(c1)) {
                return true;
            }
            c2 = c2.getSuperclass();
        }
        return false;
    }
}
