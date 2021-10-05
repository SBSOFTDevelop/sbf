package ru.sbsoft.client.components.browser;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.components.IActivateBrowser;
import ru.sbsoft.client.components.ManagedBrowser;
import ru.sbsoft.client.components.actions.event.IActivateBrowserEvent;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.components.grid.GridInfoPlugin;
import ru.sbsoft.client.components.grid.GridMode;
import ru.sbsoft.client.utils.VLC;

/**
 * Базовый класс всех самостоятельных браузеров приложения. Включает в себя таблицу данных. Предназначен для работы с выбранным множеством данных.
 *
 * @author ANONIMUS
 */
public class BaseBrowser extends Browser implements ManagedBrowser, IActivateBrowser {

    private IBaseBrowserContainer container;
    private String idBrowser;
    private String shortName;
    //
    private String caption;
    //
    private final List<IActivateBrowserEvent> activateEvents = new ArrayList<>();

    public BaseBrowser(final BaseGrid grid, GridMode... flags) {
        this(null, null, grid, flags);
    }

    public BaseBrowser(final String idBrowser, final String titleBrowser, final BaseGrid grid, GridMode... flags) {
        super(grid, flags);
        this.idBrowser = idBrowser;
        this.headerCaption = titleBrowser;
        this.caption = titleBrowser;
        this.shortName = titleBrowser;
        updateTitle();
    }

    @Override
    protected void updateTitle() {
        if (container != null) {
            container.setHeading(SafeHtmlUtils.fromString(headerCaption), null);
        }
    }

    protected void initBars(){
        add(getGridMenu());
        add(getGridToolBar(), VLC.CONST);
    }
    
    @Override
    public void init() {
        initBars();
        add(getGridContainer(), VLC.FILL);

        GridInfoPlugin.bind(getGrid(), getGridMenu());
    }

    /**
     * Задает контейнер в который помещен браузер.
     *
     * @param container
     */
    public void setContainer(IBaseBrowserContainer container) {
        this.container = container;
        updateTitle();
    }
    
    public void addActivateEvent(IActivateBrowserEvent event) {
        activateEvents.add(event);
    }

    @Override
    public void onExit() {
        if (container != null) {
            container.exit();
        }
    }

    @Override
    public void setIdBrowser(String idBrowser) {
        this.idBrowser = idBrowser;
    }

    @Override
    public String getIdBrowser() {
        return idBrowser;
    }

    @Override
    public String getShortName() {
        return shortName;
    }

    @Override
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public void onActivate() {
        activateEvents.forEach(event -> event.onActivate(this));
    }
}
