package ru.sbsoft.shared.meta;

import java.util.List;
import ru.sbsoft.shared.model.LookupInfoModel;

public class LookupListWrapper extends Wrapper<List<LookupInfoModel>> {

    public LookupListWrapper() {
    }

    public LookupListWrapper(List<LookupInfoModel> value) {
        super(value);
    }

}
