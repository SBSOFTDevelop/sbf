package ru.sbsoft.client.filter;

import ru.sbsoft.client.components.browser.SelectBrowserFactory;
import ru.sbsoft.client.components.browser.filter.lookup.LookupFieldBuilder;
import ru.sbsoft.client.components.form.LookupField;
import ru.sbsoft.shared.LookupFieldConfigBean;
import ru.sbsoft.client.components.form.handler.param.LookupHandler;
import ru.sbsoft.shared.interfaces.NamedGridType;

/**
 *
 * @author Kiselev
 */
public class HandlerLookup implements LookupFieldBuilder {

    private final String paramCode;
    private final NamedGridType browserType;

    public HandlerLookup(String paramCode, NamedGridType browserType) {
        this.paramCode = paramCode;
        this.browserType = browserType;
    }

    @Override
    public LookupField newInstance(LookupFieldConfigBean config) {
        SelectBrowserFactory f = new SelectBrowserFactory(browserType);
        f.setContext(config.getGridContext());
        if (config.getModifier() != null) {
            f.addGridModifiers(config.getModifier());
        }
        return new LookupHandler(paramCode, (String) null, f).setMultiselect(config.isMultiSelect()).getField();
    }

}
