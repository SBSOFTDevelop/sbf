package ru.sbsoft.client.components.treemenu;

import ru.sbsoft.svc.core.client.Style.LayoutRegion;
import ru.sbsoft.svc.widget.core.client.ContentPanel;
import ru.sbsoft.svc.widget.core.client.button.IconButton.IconConfig;
import ru.sbsoft.svc.widget.core.client.button.ToolButton;
import ru.sbsoft.svc.widget.core.client.event.SelectEvent.SelectHandler;
import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 * Корневой контейнер главного меню приложения. 
 * Служит для корректного перемещения меню из правой части окна в левую и обратно.
 * Используется в {@link ApplicationContainer}
 * @author balandin
 * @since Apr 15, 2013 4:35:00 PM
 */
class MenuContentPanel extends ContentPanel {

    // private ToolButton settingsButton;
    private ToolButton shiftButton;
    //
    private final LayoutRegion region;

    public MenuContentPanel(LayoutRegion region) {
        super();
        this.region = region;

        setHeading(I18n.get(SBFGeneralStr.menuCaption));
    }

    @Override
    protected void initTools() {
        super.initTools();
        addTool(getShiftButton());
        // addTool(getSettingsButton());
    }

    private static IconConfig getIcon(LayoutRegion region) {
        switch (region) {
            case EAST:
                return ToolButton.LEFT;
            case WEST:
                return ToolButton.RIGHT;
        }
        return ToolButton.QUESTION;
    }

//    public ToolButton getSettingsButton() {
//        if (settingsButton == null) {
//            settingsButton = settingsButton = new ToolButton(ToolButton.GEAR);
//        }
//        return settingsButton;
//    }

    public ToolButton getShiftButton() {
        if (shiftButton == null) {
            shiftButton = new ToolButton(getIcon(region));
        }
        return shiftButton;
    }

//    public void setAppSettingHandler(SelectHandler appSettingSelectionHandler) {
//        getSettingsButton().addSelectHandler(appSettingSelectionHandler);
//    }

    public void setAppShiftHandler(SelectHandler appShiftSelectionHandler) {
        getShiftButton().addSelectHandler(appShiftSelectionHandler);
    }
}
