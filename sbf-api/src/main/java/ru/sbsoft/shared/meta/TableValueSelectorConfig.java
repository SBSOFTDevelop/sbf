package ru.sbsoft.shared.meta;

import ru.sbsoft.shared.interfaces.GridType;

/**
 *
 * @author vk
 */
public class TableValueSelectorConfig extends AbstractValueSelectorConfig<GridType> {

    public TableValueSelectorConfig(GridType browserType) {
        super(browserType);
    }

    private TableValueSelectorConfig() {
    }
}
