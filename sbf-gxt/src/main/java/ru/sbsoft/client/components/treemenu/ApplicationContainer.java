package ru.sbsoft.client.components.treemenu;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.state.client.StateManager;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import ru.sbsoft.client.StorageProvider;
import ru.sbsoft.client.components.browser.BaseBrowserManager;
import ru.sbsoft.client.components.tree.MenuInfo;

/**
 * Главная панель приложения. 
 * Содержит главное меню и контейнер управляющий открытием и отображением браузеров {@link BaseBrowserManager}.
 * Управляет также положением главного меню.
 * Создается в {@link ru.sbsoft.client.SBFEntryPoint} при инициализации приложения.
 *
 * @author balandin
 * @since Apr 18, 2013 12:37:56 PM
 */
public class ApplicationContainer extends BorderLayoutContainer {

    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_RIGHT = 1;
    public static final int DEFAULT_WIDTH = 210;
    //
    private static final BorderLayoutData centerLayout = new BorderLayoutData();
    private static final BorderLayoutData leftLayout = new BorderLayoutData();
    private static final BorderLayoutData rightLayout = new BorderLayoutData();
    //
    private final MenuContentPanel leftContainer = new MenuContentPanel(Style.LayoutRegion.WEST);
    private final MenuContentPanel rightContainer = new MenuContentPanel(Style.LayoutRegion.EAST);
    private MenuInfo menuInfo;
    //
    private MainMenuTree mainMenu;
    private static final int BORDER_SPACE = 5;

    public ApplicationContainer(BaseBrowserManager browserManager, MainMenuTree mainMenu) {
        this(browserManager, mainMenu, ALIGN_RIGHT, DEFAULT_WIDTH);
    }

    public ApplicationContainer(BaseBrowserManager browserManager, MainMenuTree mainMenu, final int menuAlign, final int menuWidth) {
        super();
        this.mainMenu = mainMenu;

        centerLayout.setMargins(new Margins(BORDER_SPACE));

        leftLayout.setCollapsible(true);
        leftLayout.setCollapseMini(true);
        leftLayout.setFloatable(false);
        leftLayout.setMargins(new Margins(BORDER_SPACE, 0, BORDER_SPACE, BORDER_SPACE));

        rightLayout.setCollapsible(true);
        rightLayout.setCollapseMini(true);
        rightLayout.setFloatable(false);
        rightLayout.setMargins(new Margins(BORDER_SPACE, BORDER_SPACE, BORDER_SPACE, 0));

        final ResizeHandler resizeHandlerLocal = new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                if (menuInfo != null) {
                    menuInfo.setWidth(event.getWidth());
                    save();
                }
            }
        };
        final SelectHandler appShiftSelectionHandler = new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                if (menuInfo != null) {
                    menuInfo.setPosition(1 - menuInfo.getPosition());
                    shiftMenu();
                    save();
                }
            }
        };

        leftContainer.addResizeHandler(resizeHandlerLocal);
        leftContainer.setAppShiftHandler(appShiftSelectionHandler);

        rightContainer.addResizeHandler(resizeHandlerLocal);
        rightContainer.setAppShiftHandler(appShiftSelectionHandler);

        StateManager.get().setProvider(StorageProvider.getProvider(StorageProvider.StorageType.LOCAL_STORAGE));
        StateManager.get().get("menu-cfg", MenuInfo.class, new Callback<MenuInfo, Throwable>() {
            @Override
            public void onFailure(Throwable reason) {
                onSuccess(null);
            }

            @Override
            public void onSuccess(MenuInfo result) {
                if (result == null || (result.getWidth() == 0 && result.getPosition() == 0)) {
                    menuInfo = StateManager.get().getDefaultStateInstance(MenuInfo.class);
                    menuInfo.setPosition(menuAlign);
                    menuInfo.setWidth(menuWidth);
                } else {
                    menuInfo = result;
                }
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        shiftMenu();
                    }
                });
            }
        });
        setCenterWidget(browserManager, centerLayout);
        setWestWidget(leftContainer, leftLayout);
        setEastWidget(rightContainer, rightLayout);
    }

    @Override
    protected void onAfterFirstAttach() {
        super.onAfterFirstAttach();
        remove(leftContainer);
        remove(rightContainer);
    }

    private void save() {
        StateManager.get().set("menu-cfg", menuInfo);
    }

    private void shiftMenu() {
        if (menuInfo.getPosition() == ALIGN_LEFT) {
            rightContainer.removeFromParent();
            leftLayout.setSize(menuInfo.getWidth());
            setWestWidget(leftContainer, leftLayout);
            leftContainer.setWidget(mainMenu);
        } else {
            leftContainer.removeFromParent();
            rightLayout.setSize(menuInfo.getWidth());
            setEastWidget(rightContainer, rightLayout);
            rightContainer.setWidget(mainMenu);
        }
        forceLayout();
    }

//    public void setAppSettingHandler(SelectHandler h) {
//        leftContainer.setAppSettingHandler(h);
//        rightContainer.setAppSettingHandler(h);
//    }
}
