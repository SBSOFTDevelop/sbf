package ru.sbsoft.client.components.browser.filter;

import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.core.client.util.Point;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.actions.AbstractAction;
import ru.sbsoft.client.components.browser.actions.GridAction;
import ru.sbsoft.client.components.browser.filter.action.GridFilterSavedAction;
import ru.sbsoft.client.components.browser.filter.action.LoadFilterHandler;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.components.grid.GridUtils;
import ru.sbsoft.client.components.grid.dlgbase.BaseUnitWindow;
import ru.sbsoft.client.components.grid.dlgbase.DataValidationException;
import ru.sbsoft.client.components.grid.dlgbase.Unit;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.shared.FilterInfoGroup;
import ru.sbsoft.shared.FiltersBean;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.meta.filter.FilterBox;
import ru.sbsoft.shared.meta.filter.StoredFilterPath;
import ru.sbsoft.shared.model.user.Group;

/**
 *
 * @author Kiselev
 */
public class FilterWindow extends BaseUnitWindow<FilterRootGroup> {

    private final BaseGrid grid;
    private final FilterHoldersList holdersList;
    private StoredFilterPath filterPath = null;
    private FiltersBean originalFilter = null;

    public FilterWindow(BaseGrid grid) {
        super(new FilterRootGroup());
        setHeaderIcon(SBFResources.BROWSER_ICONS.BrowserFilter16());
        setHeaderText(null);

        this.grid = grid;
        this.holdersList = new FilterHoldersListFactory().create(grid);
        addStandardActions(ADD_ITEM, ADD_GROUP, DEL);
        toolBar.addSeparator();
        toolBar.addButton(new ChangeInputTypeAction());
        toolBar.addSeparator();
        toolBar.addSeparator();
        GridFilterSavedAction savedAction = new GridFilterSavedAction(grid, new LoadFilterHandler() {
            @Override
            public void set(FilterBox filter) {
                show(filter, getWindowParent(), getWinPos());
            }
        });
        savedAction.getStoredFilterSelector().addDeleteListener(new StoredFilterSelector.FilterDeleteListener() {
            @Override
            public void onFilterDelete(StoredFilterPath path) {
                if (filterPath != null && filterPath.equals(path)) {
                    init(null, null);
                }
                StoredFilterPath gridPath = getGrid().getFilterPath();
                if (gridPath != null && gridPath.equals(path)) {
                    getGrid().reload();
                }
            }
        });
        toolBar.addButton(savedAction);
        SaveAsAction saveAsAction = new SaveAsAction();
        toolBar.addButton(new SaveAction(saveAsAction));
        toolBar.addButton(saveAsAction);
    }

    private Point getWinPos() {
        return new Point(FilterWindow.this.left, FilterWindow.this.top);
    }

    private void setHeaderText(StoredFilterPath path) {
        String text = I18n.get(SBFBrowserStr.labelFilter);
        if (path != null && path.getFilterName() != null) {
            StringBuilder buf = new StringBuilder(text);
            //addHeaderText(buf, path.getIdentityName());
            addHeaderText(buf, path.getFilterName());
            text = buf.toString();
        }
        getHeader().setText(text);
    }

    private void addHeaderText(StringBuilder buf, String s) {
        final String div = " - ";
        if (s != null) {
            buf.append(div).append(s);
        }
    }

    @Override
    protected FilterItem createItem() {
        return createItem(false);
    }

    protected FilterItem createItem(boolean system) {
        FilterItem item = new FilterItem(holdersList, system);
        return system ? item : tuneItem(item);
    }

    protected BaseGrid getGrid() {
        return grid;
    }

    @Override
    protected FilterGroup createGroup() {
        FilterGroup g = new FilterGroup(null);
        g.addUnit(createItem());
        g.addUnit(createItem());
        return g;
    }

    @Override
    protected void doClearAction() {
        show(null, getWindowParent(), getWinPos());
    }

    private void init(StoredFilterPath path, FiltersBean filter) {
        filterPath = path;
        setHeaderText(path);
        originalFilter = filter;
    }

    class ChangeInputTypeAction extends AbstractAction {

        public ChangeInputTypeAction() {
            setCaption(I18n.get(SBFBrowserStr.menuTypeOfArgument));
            setToolTip(I18n.get(SBFBrowserStr.hintTypeOfArgument));
            setIcon16(SBFResources.BROWSER_ICONS.Replace16());
            setIcon24(SBFResources.BROWSER_ICONS.Replace());
        }

        @Override
        public boolean checkEnabled() {
            return check();
        }

        private boolean check() {
            Unit unit = getCurrentUnit();
            if (unit instanceof FilterItem) {
                CustomBehavior useCase = ((FilterItem) unit).getUseCase();
                if (useCase instanceof ColumnBehavior) {
                    return ((ColumnBehavior) useCase).hasSimilarFields();
                }
            }
            return false;
        }

        @Override
        protected void onExecute() {
            FilterItem item = (FilterItem) getCurrentUnit();
            ColumnBehavior useCase = (ColumnBehavior) item.getUseCase();
            if (useCase.isCompareFieldsMode()) {
                useCase.setCompareFieldsMode(false);
            } else {
                if (useCase.hasSimilarFields()) {
                    useCase.setCompareFieldsMode(true);
                }
            }
        }
    }

    
    public void quickClean() {

        rootGroup.clearAll();
        apply();
    }

    @Override
    protected void apply() {
        try {
            final FiltersBean filter = getFilter();
            if (filter == null || filter.isEmpty() || !filter.equals(originalFilter)) {
                SBFConst.CONFIG_SERVICE.saveFilter(GridUtils.getContext(grid), filter, SBFConst.DEFAULT_FILTER_PATH, new DefaultAsyncCallback<Void>() {
                    @Override
                    public void onResult(Void result) {
                        grid.setFilters(SBFConst.DEFAULT_FILTER_PATH);
                        FilterWindow.super.hide();
                    }
                });
            } else {
                grid.setFilters(filterPath);
                FilterWindow.super.hide();
            }
        } catch (DataValidationException ex) {
            //Ignore becouse message already shown by validate() method
        }
    }

    @Override
    protected boolean validate() {
        //            ClientUtils.alertWarning(I18n.get(SBFBrowserStr.msgCheckFilterConditions),
        //                    I18n.get(SBFBrowserStr.msgDataNotFilled));
        return rootGroup.validate();
    }

    private FiltersBean getFilter() throws DataValidationException {
        if (!validate()) {
            throw new DataValidationException();
        }
        final FilterInfoGroup sysFilterGroup = (FilterInfoGroup) rootGroup.getFilterInfo(true);
        final FilterInfoGroup usrFilterGroup = (FilterInfoGroup) rootGroup.getFilterInfo(false);
        return new FiltersBean(sysFilterGroup == null ? null : sysFilterGroup.getChildFilters(), usrFilterGroup);
    }

    public void show(FilterBox filter, final Widget parent) {
        this.show(filter, parent, null);
    }

    public void show(FilterBox filter, final Widget parent, Point pos) {
        rootGroup.clearValue();
        if (filter != null) {
            init(filter.getPath(), filter.getFilter());
            if (filter.getFilter() != null) {
                rootGroup.clearAll();
                new RestoreFilterCommand(this, rootGroup, filter.getFilter()).execute();
            }
        } else {
            init(null, null);
        }
        super.show(parent, pos);
    }

    private abstract class AbstractSaveAction extends GridAction {

        protected final GridContext gridContext;
        private FiltersBean filter;

        public AbstractSaveAction() {
            super(grid);
            this.gridContext = GridUtils.getContext(grid);
        }

        protected void save(final StoredFilterPath path) {
            final FiltersBean filter = this.filter;
            if (filter != null) {
                FilterWindow.this.mask(I18n.get(SBFGeneralStr.saveData));
                SBFConst.CONFIG_SERVICE.saveFilter(gridContext, filter, path, new DefaultAsyncCallback<Void>(FilterWindow.this) {
                    @Override
                    public void onResult(Void result) {
                        onSave(filter, path);
                    }
                });
            }
        }

        @Override
        protected final void onExecute() {
            try {
                filter = getFilter();
                if (filter.isEmpty()) {
                    ClientUtils.alertWarning(I18n.get(SBFBrowserStr.msgFilterEmptyCantBeSaved));
                } else {
                    exec();
                }
            } catch (DataValidationException ex) {
                //Ignore becouse message already shown by validate() method
            }
        }

        protected abstract void exec();

        protected void onSave(FiltersBean filter, StoredFilterPath path) {
        }
    }

    private class SaveAction extends AbstractSaveAction {

        private final SaveAsAction saveAsAction;

        public SaveAction(SaveAsAction saveAsAction) {
            this.saveAsAction = saveAsAction;
            setCaption(SBFBrowserStr.menuFilterSave);
            setToolTip(SBFBrowserStr.hintFilterSave);
            setIcon16(SBFResources.GENERAL_ICONS.Save16());
            setIcon24(SBFResources.GENERAL_ICONS.Save());
        }

        @Override
        protected void exec() {
            StoredFilterPath path = filterPath;
            if (path == null || path.getFilterName() == null || path.getFilterName().trim().isEmpty()) {
                saveAsAction.onExecute();
            } else {
                save(path);
            }
        }

    }

    private class SaveAsAction extends AbstractSaveAction {

        private final AbstractStoreFilterParamWindow paramWindow;

        public SaveAsAction() {
            setCaption(SBFBrowserStr.menuFilterSaveAs);
            setToolTip(SBFBrowserStr.hintFilterSaveAs);
            setIcon16(SBFResources.GENERAL_ICONS.SaveAs16());
            setIcon24(SBFResources.GENERAL_ICONS.SaveAs24());
            paramWindow = new AbstractStoreFilterParamWindow() {
                @Override
                protected void apply() {
                    Group g = group.getValue();
                    String n = name.getValue();
                    save(new StoredFilterPath(g != null ? g.getCode() : null, n));
                    hide();
                }
            };
        }

        @Override
        protected void exec() {
            paramWindow.show(gridContext, filterPath);
        }

        @Override
        protected void onSave(FiltersBean filter, StoredFilterPath path) {
            init(path, filter);
        }

    }

}
