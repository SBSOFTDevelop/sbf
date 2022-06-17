package ru.sbsoft.client.components.browser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.cell.core.client.ButtonCell;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.XTemplates.XTemplate;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.container.AbstractHtmlLayoutContainer.HtmlData;
import ru.sbsoft.svc.widget.core.client.container.BorderLayoutContainer;
import ru.sbsoft.svc.widget.core.client.container.Container;
import ru.sbsoft.svc.widget.core.client.container.HtmlLayoutContainer;
import ru.sbsoft.svc.widget.core.client.container.VerticalLayoutContainer;
import ru.sbsoft.svc.widget.core.client.event.SelectEvent;
import ru.sbsoft.svc.widget.core.client.event.UpdateEvent;
import ru.sbsoft.svc.widget.core.client.menu.MenuBarItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import ru.sbsoft.client.components.actions.Action;
import ru.sbsoft.client.components.actions.ActionManager;
import ru.sbsoft.client.components.actions.ActionMenu;
import ru.sbsoft.client.components.actions.event.KeyActionController;
import ru.sbsoft.client.components.actions.event.KeyUpDownDefinition;
import ru.sbsoft.client.components.actions.event.StdActKey;
import ru.sbsoft.client.components.browser.actions.BrowserCloseAction;
import ru.sbsoft.client.components.browser.actions.GridExpandFilterAction;
import ru.sbsoft.client.components.browser.actions.GridInfoAction;
import ru.sbsoft.client.components.browser.actions.GridQuickAggregatesAction;
import ru.sbsoft.client.components.browser.filter.FilterStatus;
import ru.sbsoft.client.components.form.BaseFormFilter;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.components.grid.CustomGridToolBar;
import ru.sbsoft.client.components.grid.GridMenu;
import ru.sbsoft.client.components.grid.GridMode;
import ru.sbsoft.client.components.grid.GridToolBar;
import ru.sbsoft.client.components.grid.IGridInfo;
import ru.sbsoft.client.components.grid.menu.GridContextMenu;
import ru.sbsoft.client.components.operation.CustomReportAction;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.CliUtil;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.client.utils.VLC;
import ru.sbsoft.shared.BooleanOperator;
import ru.sbsoft.shared.FilterInfoGroup;
import ru.sbsoft.shared.FiltersBean;
import ru.sbsoft.shared.model.CustomReportInfo;
import ru.sbsoft.shared.param.StringParamInfo;

/**
 * Базовый класс всех браузеров приложения. Является общим для независимых
 * браузеров, браузеров встроенных в формы и браузаров поисковых полей (lookup
 * select browser).
 *
 * @author balandin
 * @since May 28, 2013 3:36:31 PM
 */
public abstract class Browser extends VerticalLayoutContainer {

    protected ActionManager actionManager;
    protected Container gridContainer;
    protected final BaseGrid grid;
    //
    private GridMenu gridMenu;
    private CustomGridToolBar gridToolBar;
    private GridContextMenu gridContextMenu;
    //
    private Action closeAction;
    private Action showFilterAction2;
    private Action infoAction;
    private Action quickAggregatesAction;
    //
    protected String headerCaption;
    //
    private final List<UpdateEvent.UpdateHandler> updateToolbarHandlers = new ArrayList<>();
    //private final HTML filterInfo = new HTML();

    private final FilterInfoPanel filterInfoPanel = new FilterInfoPanel();
    private boolean initialReportFilters = false;

    private final List<StringParamInfo> reportFilters = new ArrayList<>();

//private final BorderLayoutContainer gridEnvironmentContainer = new BorderLayoutContainer();
    private final VerticalLayoutContainer gridEnvironmentContainer = new VerticalLayoutContainer();
    private boolean isHideFilter = false;
    private boolean isHideReports = false;

    private final KeyActionController keyActionController;

    public Browser(BaseGrid grid, GridMode... flags) {
        super();

        this.actionManager = new ActionManager();
        this.grid = grid;

        for (GridMode flag : flags) {
            if (flag.equals(GridMode.HIDE_FILTER)) {
                isHideFilter = true;
            }
            if (flag.equals(GridMode.HIDE_REPORTS)) {
                isHideReports = true;
            }
        }

        grid.initGrid(flags);
        grid.setActionManager(actionManager);
        grid.setDefferedContextMenu(getGridContextMenu());
        grid.setData("browser", this);

        //отложенная инициализация отчетов, если фильтры не установлены
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                if (!initialReportFilters) {
                    updateReports();
                }
            }
        });

        keyActionController = new KeyActionController(this);
        keyActionController.addAction(StdActKey.PRINT, () -> gridMenu.activateReportMenu());
        keyActionController.addAction(StdActKey.FILTER, () -> grid.showFilter());
    }

    public boolean isHideReports() {
        return isHideReports;
    }

    public void addUpdateToolbarHandler(UpdateEvent.UpdateHandler handler) {
        updateToolbarHandlers.add(handler);
    }

    public interface HtmlLayoutContainerTemplate extends XTemplates {

        //@XTemplate("<div class=\"cell1\" style=\"float: left; display: inline; height: 20px;\"></div><div class=\"cell2\" style=\"float: right; display: inline; height: 20px;\"></div>")
        @XTemplate("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" height=\"15px\"><tbody><tr><td height =\"15px\" class=\"cell1\" align=\"left\"/><td class=\"cell2\" height =\"15px\" align=\"right\"/> </tr></tbody></table>")
        SafeHtml getTemplate();
    }

    public List<StringParamInfo> getReportFilters() {
        return reportFilters;
    }

    public void setReportFilter(StringParamInfo reportFilter) {
        setReportFilters(Collections.singletonList(reportFilter));
    }

    public void setReportFilters(List<StringParamInfo> reportFilters) {
        this.reportFilters.clear();
        if (reportFilters != null && !reportFilters.isEmpty()) {
            this.reportFilters.addAll(reportFilters);
        }
        updateReports();
    }

    private class FilterInfoPanel implements IsWidget {

        private final HtmlLayoutContainerTemplate templates = GWT.create(HtmlLayoutContainerTemplate.class);
        private final HtmlLayoutContainer htmlLayoutContainer = new HtmlLayoutContainer(templates.getTemplate());
        private final HTML filterInfo = new HTML();

        class SmallButton extends TextButton {

        }

        public FilterInfoPanel() {

            TextButton btn = new TextButton();
            //btn.setScale(ButtonCell.ButtonScale.SMALL);
            btn.setToolTip("Стереть фильтр");
            //btn.addStyleName("x-btn-small");

            btn.getCell().setHeight(20);
            btn.getCell().setWidth(20);
            btn.setIconAlign(ButtonCell.IconAlign.TOP);

            btn.addAttachHandler(new AttachEvent.Handler() {
                @Override
                public void onAttachOrDetach(AttachEvent event) {

                    if (event.isAttached()) {

                        Element e = btn.getElement().childElement("div");//.removeClassName("r-c");
                        e.setClassName("");
                        //btn.getElement().childElement(".r-c").removeClassName("r-c");
                        //btn.getElement().childElement(".r-l").removeClassName("r-l");
                    }

                }
            });

            btn.setIcon(SBFResources.APP_ICONS.DeleteFile13());
            btn.addSelectHandler((SelectEvent event) -> {
                grid.clearFilter();
            });

            htmlLayoutContainer.add(filterInfo, new HtmlData(".cell1"));
            htmlLayoutContainer.add(btn, new HtmlData(".cell2"));

        }

        public void setVisible(boolean visible) {
            htmlLayoutContainer.setVisible(visible);

        }

        @Override
        public Widget asWidget() {
            return htmlLayoutContainer;
        }

    }

    public abstract void setCaption(String caption);

    /**
     * @return Визуальный компонент, содержащий таблицу данных.
     */
    protected final Container getGridContainer() {
        if (gridContainer == null) {
            Container c1 = createGridContainer();
            if (c1 instanceof BorderLayoutContainer) {
                BorderLayoutContainer bc = (BorderLayoutContainer) c1;
                if (bc.getCenterWidget() == null) {
                    bc.setCenterWidget(grid);
                }
            }
//            gridEnvironmentContainer.setCenterWidget(c1);
//            BorderLayoutContainer.BorderLayoutData filterInfoParam = new BorderLayoutContainer.BorderLayoutData(20);
//            filterInfoParam.setMinSize(20);
//            filterInfoParam.setMaxSize(20);
//            //filterInfoParam.setCollapsible(true);
//            //filterInfoParam.setCollapseMini(true);
//            //filterInfoParam.setCollapseHeaderVisible(false);
//            //filterInfoParam.setCollapseHidden(true);
//            //filterInfoParam.setCollapsed(true);
//            filterInfoParam.setHidden(true);
//            ContentPanel filterPanel = new ContentPanel();
//            filterPanel.setHeaderVisible(false);
//            filterPanel.setWidget(filterInfo);
//            gridEnvironmentContainer.setNorthWidget(filterPanel, filterInfoParam);
            filterInfoPanel.setVisible(false);
            gridEnvironmentContainer.add(filterInfoPanel, VLC.CONST_M2);
            gridEnvironmentContainer.add(c1, VLC.FILL);
            gridContainer = gridEnvironmentContainer;
        }
        return gridContainer;
    }

    protected Container createGridContainer() {
        return grid;
    }

    public GridMenu getGridMenu() {
        if (gridMenu == null) {
            gridMenu = createGridMenu();
        }
        return gridMenu;
    }

    public CustomGridToolBar getGridToolBar() {
        if (gridToolBar == null) {
            gridToolBar = createGridToolBar();
        }
        return gridToolBar;
    }

    protected GridMenu createGridMenu() {
        final GridMenu m = new GridMenu(this);
        final XElement div = DOM.createDiv().cast();
        div.getStyle().setFloat(Style.Float.RIGHT);
        div.getStyle().setDisplay(Style.Display.INLINE);
        m.getElement().appendChild(div);
        m.setData("d", div);
        return m;
    }

    protected CustomGridToolBar createGridToolBar() {
        return new GridToolBar(this, false);
    }

    protected abstract void init();

    /**
     * Задает диалог фильтра для браузера
     *
     * @param formFilter диалог фильтра
     * @deprecated doing nothing because of filter of this type is not used in
     * browsers
     */
    public void setFormFilter(BaseFormFilter formFilter) {
    }

    public abstract void onExit();

    public BaseGrid getGrid() {
        return grid;
    }

    public void enableCurrentDate() {
        // isTemporal = true;
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public Action getCloseAction() {
        if (closeAction == null) {
            closeAction = new BrowserCloseAction(this);
        }
        return closeAction;
    }

    public Action getShowFilterAction2() {

        if (showFilterAction2 == null && !isHideFilter) {
            showFilterAction2 = new GridExpandFilterAction(this);
        }
        return showFilterAction2;
    }

    public Action getQuickAggregatesAction() {
        if (quickAggregatesAction == null && getGrid().isQuickAggregateAllowed()) {
            quickAggregatesAction = new GridQuickAggregatesAction(grid);
        }
        return quickAggregatesAction;
    }

    public GridContextMenu getGridContextMenu() {
        if (gridContextMenu == null) {
            GridContextMenu contextMenu = new GridContextMenu(this);
            List<Action> actions = getContextCustomActions();
            if (actions != null) {
                contextMenu.addExtraActions(actions);
            }
            gridContextMenu = contextMenu;
        }
        return gridContextMenu;
    }

    public Action getInfoAction() {
        if (infoAction == null) {
            this.infoAction = new GridInfoAction(this);
        }
        return infoAction;
    }

    protected abstract void updateTitle();

    private void setFilterInfo(SafeHtml inf, String toolTip) {
        if (CliUtil.hasText(inf)) {
            filterInfoPanel.filterInfo.setHTML(inf);
            filterInfoPanel.filterInfo.setTitle(toolTip);
            filterInfoPanel.setVisible(true);
            //gridEnvironmentContainer.show(ru.sbsoft.svc.core.client.Style.LayoutRegion.NORTH);
        } else {
            filterInfoPanel.setVisible(false);
            //gridEnvironmentContainer.hide(ru.sbsoft.svc.core.client.Style.LayoutRegion.NORTH);
        }
        forceLayout();
    }

    public void updateFilterStatus(IGridInfo grid, FiltersBean filters) {
        SafeHtml filterInf = null;
        String toolTip = null;
        FilterInfoGroup g = new FilterInfoGroup(BooleanOperator.AND);
        if (filters != null) {
            g.getChildFilters().addAll(filters.getSystemFilters());
            g.getChildFilters().addAll(filters.getUserFilters().getChildFilters());
            if (!g.getChildFilters().isEmpty()) {
                FilterStatus filterStatus = new FilterStatus(grid);
                filterInf = filterStatus.generateHtml(g);
                toolTip = filterStatus.setMultiLine().generatePlain(g);
            }
        }
        setFilterInfo(filterInf, toolTip);
    }

    protected List<Action> getContextCustomActions() {
        return null;
    }

    private void updateReports() {
        initialReportFilters = true;
        if (isHideReports) {
            return;
        }
        grid.loadCustomReports(getReportFilters(), new DefaultAsyncCallback<List<CustomReportInfo>>(this) {
            @Override
            public void onResult(List<CustomReportInfo> result) {
                super.onResult(result);
                Action[] actions = new Action[result != null ? result.size() : 0];
                if (result != null) {
                    for (int i = 0; i < result.size(); i++) {
                        actions[i] = new CustomReportAction(grid, result.get(i));
                    }
                }
                if (getGridMenu() != null) {
                    getGridMenu().updateReportActions(actions);
                }
                CustomGridToolBar gridToolBar = getGridToolBar();
                if (gridToolBar instanceof GridToolBar) {
                    ((GridToolBar) gridToolBar).updateReportActions(actions);
                    updateToolbarHandlers.forEach(h -> h.onUpdate(new UpdateEvent()));
                }
            }

        });
    }

//    public void updateFilterStatus(IGridInfo grid, FiltersBean generateHtml) {
//        headerDescription = null;
//        headerTooltip = null;
//
//        FilterInfoGroup g = new FilterInfoGroup(BooleanOperator.AND);
//        if (generateHtml != null) {
//            g.getChildFilters().addAll(generateHtml.getSystemFilters());
//            g.getChildFilters().addAll(generateHtml.getUserFilters().getChildFilters());
//
//            if (!g.getChildFilters().isEmpty()) {
//                SafeHtml cap = FilterStatus.generateHtml(grid, g, false, headerCaption);
//                SafeHtml tip = FilterStatus.generateHtml(grid, g, true, null);
//                headerDescription = cap;
//                headerTooltip = tip;
//            }
//        }
//        updateTitle();
//    }
}
