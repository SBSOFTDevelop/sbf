package ru.sbsoft.client.components;

import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.interfaces.NamedGridType;

/**
 *
 * @author vk
 */
public abstract class AbstractNamedGridBrowserMaker implements IBrowserMaker {

    protected final NamedGridType gridType;

    protected AbstractNamedGridBrowserMaker(NamedGridType gridType) {
        this.gridType = gridType;
    }

    @Override
    public String getIdBrowser() {
        return gridType.getCode();
    }

    @Override
    public String getTitleBrowser() {
        return I18n.get(gridType.getItemName());
    }

    @Override
    public String getSecurityId() {
        return gridType.getSecurityId();
    }
}
