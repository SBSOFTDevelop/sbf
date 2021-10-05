package ru.sbsoft.shared.filter;

import ru.sbsoft.shared.FilterInfo;
import java.util.List;
import ru.sbsoft.shared.FilterTypeEnum;
import ru.sbsoft.shared.model.LookupInfoModel;

public class LookUpFilterInfo extends FilterInfo<List<LookupInfoModel>> {

    private long tmp1 = -1;
    private long tmp2 = -1;

    public LookUpFilterInfo() {
        this(null, null);
    }

    public LookUpFilterInfo(String columnName, List<LookupInfoModel> value) {
        super(columnName, null, FilterTypeEnum.LOOKUP, value);
    }
    
    public long getTmp1() {
        return tmp1;
    }

    public void setTmp1(long tmp1) {
        this.tmp1 = tmp1;
    }

    public long getTmp2() {
        return tmp2;
    }

    public void setTmp2(long tmp2) {
        this.tmp2 = tmp2;
    }
}
