package ru.sbsoft.client.components.browser;

import java.math.BigDecimal;
import java.util.List;
import ru.sbsoft.client.components.form.LookupField;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.shared.model.LookupInfoModel;

/**
 * Базовый класс для браузеров-справочников поисковых полей.
 * @see LookupField
 * @param <M> модель данных
 */
public class CodebaseSelectBrowser<M extends LookupInfoModel> extends SelectBaseBrowser<M> {

    public CodebaseSelectBrowser(String caption, BaseGrid grid) {
        this(caption, grid, false);
    }

    public CodebaseSelectBrowser(String caption, BaseGrid grid, List<BigDecimal> values) {
        this(caption, grid, true);
        grid.setMarkedRecords(values);
    }

    public CodebaseSelectBrowser(String caption, BaseGrid grid, boolean isMultiSelect) {
        super(grid, isMultiSelect);
        setCaption(caption);
    }
}
