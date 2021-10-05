package ru.sbsoft.shared.param;

import java.util.List;

public class ListParamInfo<M> extends ParamInfo<List<M>> {

    public ListParamInfo() {
        this(null, null);
    }

    public ListParamInfo(final String name, final List<M> value) {
        super(name, ParamTypeEnum.LIST, value);
    }
};
