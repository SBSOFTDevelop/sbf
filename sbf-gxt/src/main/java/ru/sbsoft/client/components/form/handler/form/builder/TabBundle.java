package ru.sbsoft.client.components.form.handler.form.builder;

import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author Kiselev
 * @param <DataModel>
 */
public class TabBundle<DataModel extends IFormModel> {

    private final List<Tab<DataModel>> tabs = new ArrayList<>();

    public TabBundle<DataModel> add(Tab<DataModel> set) {
        tabs.add(set);
        return this;
    }

    public List<Tab<DataModel>> getTabs() {
        return tabs;
    }
}
