package ru.sbsoft.client.components.form.handler.form.builder;

import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author Kiselev
 * @param <DataModel>
 */
public class FSetBundle<DataModel extends IFormModel> {

    private final List<FSet<DataModel>> sets = new ArrayList<>();

    public FSetBundle<DataModel> add(FSet<DataModel> set) {
        sets.add(set);
        return this;
    }

    public List<FSet<DataModel>> getSets() {
        return sets;
    }

}
