package ru.sbsoft.shared.model;

import ru.sbsoft.shared.FilterTypeEnum;

/**
 * @author balandin
 * @since Aug 15, 2013 7:36:00 PM
 */
public enum LookupCellType {

    KEY(FilterTypeEnum.LOOKUP_CODE),
    NAME(FilterTypeEnum.LOOKUP_NAME);
    //
    private final FilterTypeEnum filterType;

    private LookupCellType(FilterTypeEnum filterType) {
        this.filterType = filterType;
    }

    public FilterTypeEnum getFilterType() {
        return filterType;
    }
}
