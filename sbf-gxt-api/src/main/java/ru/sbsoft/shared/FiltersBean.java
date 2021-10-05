package ru.sbsoft.shared;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import ru.sbsoft.common.Defs;
import ru.sbsoft.shared.param.DTO;

/**
 * @author balandin
 * @since Nov 17, 2015
 */
public class FiltersBean implements DTO {

    private List<FilterInfo> systemFilters;
    private FilterInfoGroup userFilters;
    private transient Map<String, FilterInfo> sysCache;

    public FiltersBean() {
    }

    public FiltersBean(List<FilterInfo> systemFilters, FilterInfoGroup userFilters) {
        this.systemFilters = systemFilters;
        this.userFilters = userFilters;
    }

    public List<FilterInfo> getSystemFilters() {
        if (systemFilters == null) {
            return Defs.coalesce(systemFilters, Collections.EMPTY_LIST);
        }
        return systemFilters;
    }

    public void setSystemFilters(List<FilterInfo> systemFilters) {
        this.systemFilters = systemFilters;
    }

    public FilterInfoGroup getUserFilters() {
        if (userFilters == null) {
            return new FilterInfoGroup(BooleanOperator.AND);
        }
        return userFilters;
    }

    public void setUserFilters(FilterInfoGroup userFilters) {
        this.userFilters = userFilters;
    }

    public void clear() {
        systemFilters = null;
        userFilters = null;
    }

    public Map<String, FilterInfo> getSysCache() {
        if (sysCache == null) {
            sysCache = makeSysCache();
        }
        return sysCache;
    }

    private Map<String, FilterInfo> makeSysCache() {
        Map<String, FilterInfo> r = new HashMap<String, FilterInfo>();
        for (FilterInfo item : getSystemFilters()) {
            r.put(item.getColumnName(), item);
        }
        return r;
    }

    public static FiltersBean create(FilterInfo... filters) {
        return create(Arrays.asList(filters));
    }

    public static FiltersBean create(Collection<FilterInfo> filters) {
        FilterInfoGroup usrGroup = new FilterInfoGroup(BooleanOperator.AND);
        if (filters != null) {
            usrGroup.getChildFilters().addAll(filters);
        }

        FiltersBean r = new FiltersBean();
        r.setUserFilters(usrGroup);
        return r;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.systemFilters != null ? this.systemFilters.hashCode() : 0);
        hash = 23 * hash + (this.userFilters != null ? this.userFilters.hashCode() : 0);
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
        final FiltersBean other = (FiltersBean) obj;
        if (this.systemFilters != other.systemFilters && (this.systemFilters == null || other.systemFilters == null || !new HashSet<FilterInfo>(this.systemFilters).equals(new HashSet<FilterInfo>(other.systemFilters)))) {
            return false;
        }
        return !(this.userFilters != other.userFilters && (this.userFilters == null || !this.userFilters.equals(other.userFilters)));
    }

    public boolean isEmpty(){
        if(userFilters != null && !userFilters.isEmpty()){
            return false;
        }
        if(systemFilters != null && !systemFilters.isEmpty()){
            for(FilterInfo inf : systemFilters){
                if(!inf.isEmpty()){
                    return false;
                }
            }
        }
        return true;
    }
}
