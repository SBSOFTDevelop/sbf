package ru.sbsoft.shared.filter;

import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterTypeEnum;

/**
 * @author balandin
 * @since Jul 15, 2015
 */
public class FilterInfoImpl extends FilterInfo<String> {

    public FilterInfoImpl() {
    }

    public FilterInfoImpl(FilterTypeEnum type) {
        super(type);
    }
}
