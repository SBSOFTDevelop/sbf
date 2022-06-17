package ru.sbsoft.client.components.browser;

import ru.sbsoft.svc.widget.core.client.container.BorderLayoutContainer;
import ru.sbsoft.svc.widget.core.client.container.Container;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.BaseWindow;
import ru.sbsoft.client.components.ILookupField;
import ru.sbsoft.client.components.ISelectBrowser;
import ru.sbsoft.client.components.actions.AbstractAction;
import ru.sbsoft.client.components.actions.Action;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.components.grid.GridEvent;
import ru.sbsoft.client.components.grid.GridInfoPlugin;
import ru.sbsoft.client.components.grid.GridMode;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.VLC;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFFormStr;
import ru.sbsoft.shared.model.LookupInfoModel;

import java.util.List;
import java.util.Set;

/**
 * Базовый класс для браузеров поисковых полей.
 *
 * @author Kiselev
 * @param <M> модель данных
 */
public class SelectBaseBrowser<M extends LookupInfoModel> extends Browser implements ISelectBrowser<M> {

    private BaseWindow window;
    private ILookupField<M> lookupField;
    //

    public void perfomSelectAction() {
        lookupField.selectValue(new Runnable() {
            @Override
            public void run() {
                onExit();
            }
        });
    }

    private final Action doubleClickAction = new AbstractAction() {
        @Override
        protected void onExecute() {
            perfomSelectAction();
        }
    };

    public SelectBaseBrowser(final BaseGrid grid, final boolean isMultiSelect) {
        this(grid, GridMode.getSingleSet(!isMultiSelect));
    }

    public SelectBaseBrowser(final BaseGrid grid, Set<GridMode> flags) {
        super(grid, flags.toArray(new GridMode[]{}));
    }

    public SelectBaseBrowser(final BaseGrid grid, GridMode... flags) {
        super(grid, flags);
    }

    @Override
    protected void init() {
        add(getGridMenu());
        add(getGridToolBar(), VLC.CONST);
        add(getGridContainer(), VLC.FILL);

        GridInfoPlugin.bind(getGrid(), getGridMenu());
    }

    @Override
    protected void onAfterFirstAttach() {
        init();
        super.onAfterFirstAttach();
    }

    @Override
    public void setOwnerLookupField(ILookupField<M> owner) {
        this.lookupField = owner;
    }

    public void setParentFilters(List<FilterInfo> parentFilters) {
        grid.setParentFilters(parentFilters);
    }

    @Override
    public void show() {
        final BaseWindow w = getWindow();
        if (w.getWindow().getWidget() == null) {
            w.getWindow().setWidget(this);
        }
        final Container c = getGridContainer();
        if (c instanceof BorderLayoutContainer) {
            ((BorderLayoutContainer) c).setCenterWidget(grid);
            ((BorderLayoutContainer) c).forceLayout();
        } else if (getWidgetIndex(c) == -1) {
            add(c, VLC.FILL);
        }

        grid.initEventMap();
        grid.bindEvent(GridEvent.DOUBLE_CLICK, doubleClickAction);
        grid.bindEvent(GridEvent.EDIT, doubleClickAction);
        grid.setDefferedContextMenu(getGridContextMenu());

        grid.setLoadHandler(null);
        grid.checkInitialized();

        w.show();
    }

    @Override
    public void onExit() {
        if (window != null) {
            window.hide();
        }
    }

    @Override
    public void setCaption(String caption) {
        this.headerCaption = I18n.get(SBFFormStr.labelSelect) + " - " + caption;
        updateTitle();
    }

    public BaseWindow getWindow() {
        if (window == null) {
            window = new BaseWindow(this);
            window.setPixelSize(800, 600);
            window.setModal(true);
            window.getWindow().getHeader().setIcon(SBFResources.APP_ICONS.Table());
            updateTitle();
        }
        return window;
    }

    @Override
    protected void updateTitle() {
        if (window == null) {
            return;
        }
        window.setHeading(headerCaption == null || (headerCaption = headerCaption.trim()).isEmpty() ? "" : headerCaption);
    }
}
