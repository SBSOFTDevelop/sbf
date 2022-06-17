package ru.sbsoft.client.components.grid;

import ru.sbsoft.shared.consts.Dict;
import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author Fedor Resnyanskiy
 */
public class DefaultCodeProvider implements IFormCodeProvider {

    private final static String DEFAULT_COLUMN_NAME = Dict.DTYPE;
    private String formCodeColumnName = DEFAULT_COLUMN_NAME;

    public String getFormCodeColumnName() {
        return formCodeColumnName;
    }

    public void setFormCodeColumnName(String formCodeColumnName) {
        this.formCodeColumnName = formCodeColumnName;
    }

    @Override
    public String getFormCodeForRow(Row row) {
        return row.getString(getFormCodeColumnName());
    }
}
