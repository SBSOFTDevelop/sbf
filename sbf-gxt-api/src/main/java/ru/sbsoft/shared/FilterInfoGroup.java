package ru.sbsoft.shared;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FilterInfoGroup extends FilterInfo<BooleanOperator> {

    private List<FilterInfo> childFilters;

    public FilterInfoGroup() {
        this(null);
    }

    public FilterInfoGroup(BooleanOperator booleanOperator) {
        super(null, null, null, booleanOperator);
    }

    public FilterInfoGroup(BooleanOperator booleanOperator, List<FilterInfo> childs) {
        super(null, null, null, booleanOperator);
        this.childFilters = childs;
    }

    public List<FilterInfo> getChildFilters() {
        if (childFilters == null) {
            childFilters = new ArrayList<FilterInfo>();
        }
        return childFilters;
    }

    public void setChildFilters(List<FilterInfo> childFilters) {
        this.childFilters = childFilters;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        if (childFilters != null) {
            for (FilterInfo inf : childFilters) {
                if (inf != null) {
                    hash += inf.hashCode();
                }
            }
        }
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
        final FilterInfoGroup other = (FilterInfoGroup) obj;
        if(this.childFilters == other.childFilters){
            return true;
        }
        if(this.childFilters == null || other.childFilters == null){
            return false;
        }
        if(this.childFilters.size() != other.childFilters.size()){
            return false;
        }
        return new HashSet<FilterInfo>(this.childFilters).equals(new HashSet<FilterInfo>(other.childFilters));
    }

    @Override
    public boolean isEmpty() {
        if(childFilters == null || childFilters.isEmpty()){
            return true;
        }
        for(FilterInfo inf : childFilters){
            if(inf != null && !inf.isEmpty()){
                return false;
            }
        }
        return true;
    }

}
