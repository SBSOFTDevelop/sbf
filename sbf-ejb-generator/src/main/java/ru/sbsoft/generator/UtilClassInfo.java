package ru.sbsoft.generator;

import java.util.Collections;
import java.util.Set;

/**
 *
 * @author Kiselev
 */
public class UtilClassInfo {
    private final String name;
    private final Set<LookupFieldInfo> fields;
    private final Set<InjectionInfo> injections;

    public UtilClassInfo(String name, Set<LookupFieldInfo> fields, Set<InjectionInfo> injections) {
        this.name = name;
        this.fields = fields != null ? fields : Collections.<LookupFieldInfo>emptySet();
        this.injections = injections != null ? injections : Collections.<InjectionInfo>emptySet();
    }

    public String getName() {
        return name;
    }

    public Set<LookupFieldInfo> getFields() {
        return fields;
    }

    public Set<InjectionInfo> getInjections() {
        return injections;
    }
}
