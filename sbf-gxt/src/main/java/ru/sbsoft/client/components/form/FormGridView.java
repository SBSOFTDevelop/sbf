package ru.sbsoft.client.components.form;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import ru.sbsoft.client.components.actions.Action;
import ru.sbsoft.client.components.browser.Browser;
import ru.sbsoft.client.components.form.fields.ReadOnlyControl;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.components.grid.CustomGridToolBar;
import ru.sbsoft.client.components.grid.GridInfoPlugin;
import ru.sbsoft.client.components.grid.GridMenu;
import ru.sbsoft.client.components.grid.GridMode;
import ru.sbsoft.client.components.grid.SBGrid;
import ru.sbsoft.client.components.grid.VerticalGridToolBar;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.CliUtil;
import ru.sbsoft.client.utils.HLC;
import ru.sbsoft.client.utils.VLC;

/**
 * Визуальный контейнер для встроенной в форму таблицы данных.
 *
 * @author Kiselev
 */
public class FormGridView extends Browser implements ReadOnlyControl {

    private final ContentPanel contentPanel = new ContentPanel();
    private final boolean compactView;
    private final boolean noMenu;
    private Boolean headerVisible = null;

    public FormGridView(final BaseGrid grid, boolean compactView, GridMode... flags) {
        this(grid, compactView, false, flags);
    }

    public FormGridView(BaseGrid grid, boolean compactView, boolean noMenu, GridMode... flags) {
        super(grid, flags);
        this.compactView = compactView;
        this.noMenu = noMenu;
        contentPanel.setHeaderVisible(false);
        checkGridInitialized();
    }
    
    private void checkGridInitialized(){
        Scheduler.get().scheduleDeferred(() -> {
            SBGrid g = grid.getGrid();
            if(grid.isInitialized() && g.isVisible(true)){
                g.getView().positionLiveScroller();
            }else{
                checkGridInitialized();
            }
        });
    }

    @Override
    protected void onAfterFirstAttach() {
        init();
        super.onAfterFirstAttach();
    }

    @Override
    protected GridMenu createGridMenu() {
        return (compactView || noMenu) ? null : super.createGridMenu();
    }

    @Override
    protected CustomGridToolBar createGridToolBar() {
        return compactView ? new VerticalGridToolBar(this, false) : super.createGridToolBar();
    }

    @Override
    protected void init() {
        VerticalLayoutContainer vContainer = new VerticalLayoutContainer();

        if (compactView) {
            HorizontalLayoutContainer hContainer = new HorizontalLayoutContainer();
            hContainer.setScrollMode(ScrollSupport.ScrollMode.NONE);
            hContainer.add(getGridContainer(), HLC.FILL);
            hContainer.add(getGridToolBar(), HLC.CONST);
            vContainer.add(hContainer, VLC.FILL);

            GridInfoPlugin.bind(grid, contentPanel);
        } else {
            if (null != getGridMenu()) {
                vContainer.add(getGridMenu());
                GridInfoPlugin.bind(grid, getGridMenu());
            } else {
                GridInfoPlugin.bind(grid, contentPanel);
            }
            vContainer.add(getGridToolBar(), VLC.CONST);
            vContainer.add(getGridContainer(), VLC.FILL);
        }

        contentPanel.getHeader().setIcon(SBFResources.APP_ICONS.Table());
        contentPanel.setWidget(vContainer);

        add(contentPanel, VLC.FILL);
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        getGrid().setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() {
        return getGrid().isReadOnly();
    }

    @Override
    public void onExit() {
    }

    @Override
    public Action getCloseAction() {
        return null;
    }

    public void refresh() {
        grid.checkInitialized();
    }

    @Override
    public void setCaption(String caption) {
        setHeadingText(caption);
    }

    public void setHeadingText(String caption) {
        headerCaption = caption;
        updateTitle();
        updateHeaderVisibility();
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        updateHeaderVisibility();
    }

    private void updateHeaderVisibility() {
        if (headerVisible == null) {
            CliUtil.updateHeaderVisibility(contentPanel);
        } else {
            boolean change = headerVisible ^ contentPanel.getHeader().isVisible();
            if (change) {
                contentPanel.setHeaderVisible(headerVisible);
                contentPanel.getHeader().setVisible(headerVisible);
                if (compactView || noMenu) {
                    contentPanel.getHeader().setHeight(20);
                }

                forceLayout();
            }
        }
    }

    public void setHeaderVisible(boolean value) {
        headerVisible = value;
        updateHeaderVisibility();
    }

    @Override
    protected void updateTitle() {
        contentPanel.setHeading(headerCaption == null || (headerCaption = headerCaption.trim()).isEmpty() ? "" : headerCaption);
    }
}
