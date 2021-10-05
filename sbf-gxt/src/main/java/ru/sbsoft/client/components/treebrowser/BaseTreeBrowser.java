package ru.sbsoft.client.components.treebrowser;

import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.components.IActivateBrowser;
import ru.sbsoft.client.components.ManagedBrowser;
import ru.sbsoft.client.components.actions.event.IActivateBrowserEvent;
import ru.sbsoft.sbf.gxt.utils.VLC;

/**
 *
 * @author sokolov
 */
public abstract class BaseTreeBrowser extends TreeBrowser implements ManagedBrowser, IActivateBrowser {

    private String idBrowser;
    private String shortName;
    private String caption;
    //
    private final BaseTreeBrowserFactory browserFactory;
    //
    private final List<IActivateBrowserEvent> activateEvents = new ArrayList<>();

    public BaseTreeBrowser(final BaseTreeBrowserFactory browserFactory, final String idBrowser, final String titleBrowser) {
        super(false);
        this.browserFactory = browserFactory;
        this.idBrowser = idBrowser;
        this.shortName = titleBrowser;
        this.caption = titleBrowser;
    }
    
    public void addActivateEvent(IActivateBrowserEvent event) {
        activateEvents.add(event);
    }

    @Override
    protected void init() {
        grid = createGrid();
        grid.setActionManager(actionManager);
        initBars();
        VerticalLayoutContainer vc = new VerticalLayoutContainer();
        vc.getElement().setPadding(new Padding(3, 4, 0, 4));
        add(vc, VLC.FILL);
        vc.add(grid, VLC.FILL);
        browserFactory.initActions(this);
        grid.refresh(true);
        forceLayout();
    }

    @Override
    public void setIdBrowser(String value) {
        idBrowser = value;
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
    public void setShortName(String value) {
        shortName = value;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String value) {
        caption = value;
    }
    
    @Override
    public void onActivate() {
        activateEvents.forEach(event -> event.onActivate(this));
    }

}
