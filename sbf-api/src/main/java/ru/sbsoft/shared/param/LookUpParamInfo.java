package ru.sbsoft.shared.param;

import java.util.List;
import ru.sbsoft.shared.model.LookupInfoModel;

public class LookUpParamInfo extends ParamInfo<List<LookupInfoModel>> {

    public LookUpParamInfo() {
        setType(ParamTypeEnum.LOOKUP);
    }
}
