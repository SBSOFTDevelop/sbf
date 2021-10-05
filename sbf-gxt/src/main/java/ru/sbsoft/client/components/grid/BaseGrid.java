package ru.sbsoft.client.components.grid;

import ru.sbsoft.sbf.app.model.FormModel;
import ru.sbsoft.sbf.app.ICondition;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.menu.Menu;
import java.math.BigDecimal;
import java.util.*;
import ru.sbsoft.client.components.IWindow;
import ru.sbsoft.client.components.actions.Action;
import ru.sbsoft.client.components.actions.ActionManager;
import ru.sbsoft.client.components.browser.actions.GridCloneAction;
import ru.sbsoft.client.components.browser.actions.GridDeleteAction;
import ru.sbsoft.client.components.browser.actions.GridInsertAction;
import ru.sbsoft.client.components.browser.actions.GridMarkAction;
import ru.sbsoft.client.components.browser.actions.GridMarkClearAction;
import ru.sbsoft.client.components.browser.actions.GridMarkInverseAllAction;
import ru.sbsoft.client.components.browser.actions.GridMoveFirstAction;
import ru.sbsoft.client.components.browser.actions.GridMoveFirstMarkAction;
import ru.sbsoft.client.components.browser.actions.GridMoveLastAction;
import ru.sbsoft.client.components.browser.actions.GridMoveLastMarkAction;
import ru.sbsoft.client.components.browser.actions.GridMoveNextAction;
import ru.sbsoft.client.components.browser.actions.GridMoveNextMarkAction;
import ru.sbsoft.client.components.browser.actions.GridMovePrevAction;
import ru.sbsoft.client.components.browser.actions.GridMovePrevMarkAction;
import ru.sbsoft.client.components.browser.actions.GridRefreshAction;
import ru.sbsoft.client.components.browser.actions.GridReloadAction;
import ru.sbsoft.client.components.browser.actions.GridShowAction;
import ru.sbsoft.client.components.browser.actions.GridUpdateAction;
import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.client.components.form.IBaseFormOwner;
import ru.sbsoft.sbf.app.form.IHandler;
import ru.sbsoft.client.components.grid.menu.GridContextMenu;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.sbf.app.form.controller.IFormSaveController.PersistEvent;
import ru.sbsoft.sbf.app.form.controller.IFormSaveController.SaveEvent;
import ru.sbsoft.shared.*;
import ru.sbsoft.shared.exceptions.ThrowableWrapper;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.meta.Columns;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.meta.filter.StoredFilterPath;
import ru.sbsoft.shared.model.*;
import ru.sbsoft.shared.model.operation.OperationCommand;
import ru.sbsoft.shared.services.FetchParams;

/**
 * Наполняет функционалом {@link ContextGrid}
 *
 * @param <DataInfoModel> модель данных
 */
public abstract class BaseGrid<DataInfoModel extends MarkModel> extends SystemGrid<DataInfoModel> implements ClientUtils.ErrorHandler, IGridInfo, IBaseFormOwner {

    public static boolean ALLOW_SELECTION = true;

    private static final StoredFilterPath DEFAULT_FILTER_PATH = SBFConst.DEFAULT_FILTER_PATH;
    //
    private final List<BaseForm> openedForms = new ArrayList<BaseForm>();
    private final List<EditFormFactoryInfo> formFactories = new ArrayList<EditFormFactoryInfo>();
    //
    private ActionManager actionManager;

    private final Map<GridEvent, Action> actionsMap = new EnumMap<GridEvent, Action>(GridEvent.class);
    //
    private Action refreshAction;
    private Action reloadGridAction = null;
    private Action moveFirstAction;
    private Action moveLastAction;
    private Action moveNextAction;
    private Action movePrevAction;
    private Action markCheckAction;
    private Action markClearAction;
    private Action markInverseAllAction;
    private Action moveFirstMarkAction;
    private Action moveLastMarkAction;
    private Action moveNextMarkAction;
    private Action movePrevMarkAction;
    //
    private Action insertAction;
    private Action cloneAction;
    private Action updateAction;
    private Action showAction;
    private Action deleteAction;
    //
    private GridContextMenu gridContextMenu;
    private boolean contextMenuInitialized = false;
    private boolean isOnlyShow = false;
    private Event lastRightClickEvent;

    public BaseGrid() {
        getGrid().getView().setBaseGrid(this);
        disableContextMenu(true);
    }

    public void bindEvent(GridEvent event, Action action) {
        if (action == null) {
            actionsMap.remove(event);
        } else {
            actionsMap.put(event, action);
        }
    }

    public void setFilters(final FiltersBean filters) {
        CONFIG_RPC.saveFilter(getGridContext(), filters, DEFAULT_FILTER_PATH, new DefaultAsyncCallback<Void>() {
            @Override
            public void onResult(Void result) {
                setFilters((StoredFilterPath) DEFAULT_FILTER_PATH);
            }
        });
    }

    public void setFilters(final StoredFilterPath filterPath) {
        final GridContext context = getGridContext();
        CONFIG_RPC.setCurrentFilter(context, filterPath, new DefaultAsyncCallback<Void>() {
            @Override
            public void onResult(Void result) {
                reload();
            }
        });
    }

    public void viewForm(final boolean isInsert) {
        final DataInfoModel model = getGrid().getSelectionModel().getSelectedItem();
        if (model != null) {
            createEditForm(model, new DefaultAsyncCallback<BaseForm>() {
                @Override
                public void onResult(BaseForm form) {
                    bindEditForm(form, model);
                    form.setReadOnly(true);
                    onBeforeShowForm(form);
                    form.show(model.getRECORD_ID());
                }
            });
        }
    }

    public void bindEditForm(final BaseForm form, final DataInfoModel model) {
        if (null == form) {
            return;
        }
        openedForms.add(form);
        form.setOwnerGrid(this);
        form.addReadOnlyCondition(new ICondition() {
            @Override
            public boolean check() {
                return isReadOnly(true);
            }
        });
        form.addSaveHandler(new IHandler<SaveEvent>() {
            @Override
            public void onHandle(SaveEvent event) {
                if (model != null) {
                    refreshRow(model.getRECORD_ID());
                }
            }
        });
        form.addHideHandler(new IWindow.WindowHideHandler() {
            @Override
            public void onHide(IWindow.WindowHideEvent event) {
                if (model != null) {
                    refreshRow(model.getRECORD_ID());
                }
                openedForms.remove(form);
            }
        });
        form.addPersistHandler(new IHandler<PersistEvent<FormModel>>() {
            @Override
            public void onHandle(PersistEvent<FormModel> event) {
                addRowFromModel(event.getModel());
            }
        });
    }

    private void addRowFromModel(FormModel model) {
        if (isInitialized() && model != null && model.getId() != null) {
            loadAddedRow(model);
        }
    }

    public void edit() {
        final DataInfoModel model = getGrid().getSelectionModel().getSelectedItem();
        tryShowForm(model);
    }

//    public void insert() {
//        insert(null);
//    }
    public void insert() {

        if (formFactories.isEmpty()) {
            tryShowForm(null);
            return;
        }

        final List<EditFormFactoryInfo> insertableFormFactoryInfos = new ArrayList<EditFormFactoryInfo>();
        for (EditFormFactoryInfo formFactoryInfo : formFactories) {
            if (formFactoryInfo.isInsertable()) {
                insertableFormFactoryInfos.add(formFactoryInfo);
            }
        }

        if (insertableFormFactoryInfos.size() == 1) {
            final BaseForm form = insertableFormFactoryInfos.get(0).getFormFactory().createForm();
            showForm(form, null);
        } else {
            final List<DefaultSelectForm.FormInfo> formInfoList = new ArrayList<DefaultSelectForm.FormInfo>();
            for (EditFormFactoryInfo formFactoryInfo : insertableFormFactoryInfos) {
                formInfoList.add(new DefaultSelectForm.FormInfo(formFactoryInfo.getName(), formFactoryInfo.getFormFactory()));
            }
            final DefaultSelectForm selectWindow = new DefaultSelectForm(formInfoList);
            selectWindow.addSelectFormHandler(new SelectFormHandler() {
                @Override
                public void onSelect(EditFormFactoryInfo.FormFactory formFactory) {
                    showForm(formFactory.createForm(), null);
                }
            });
            selectWindow.show();
        }
    }

    public void showForm(final BigDecimal ID) {
        loadSingleRow(ID, new DefaultAsyncCallback<MarkModel>() {

            @Override
            public void onResult(MarkModel result) {
                if (result instanceof Row) {
                    final Row row = (Row) result;
                    if (row.getColumns() == null) {
                        RPC.getMeta(getGridContext(), new DefaultAsyncCallback<Columns>() {

                            @Override
                            public void onResult(Columns result) {
                                row.setColumns(result);
                                tryShowForm((DataInfoModel) row);
                            }
                        });
                        return;
                    }
                }
                tryShowForm((DataInfoModel) result);
            }
        });
    }

    protected boolean tryShowForm(final DataInfoModel model) {
        showForm(model);
        return true;
    }

    protected void showForm(final DataInfoModel model) {
        getEditForm(model, new DefaultAsyncCallback<BaseForm>() {
            @Override
            public void onResult(BaseForm form) {
                showForm(form, model);
            }
        });
    }

    protected void showForm(final BaseForm form, final DataInfoModel model) {
        if (null != form) {
            bindEditForm(form, model);
            onBeforeShowForm(form);
            form.show(null == model ? null : model.getRECORD_ID());
        }
    }

    protected void onBeforeShowForm(BaseForm form) {
    }

    protected void getEditForm(final DataInfoModel model, AsyncCallback<BaseForm> callback) {
        createEditForm(model, new AsyncCallback<BaseForm>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(BaseForm form) {
                if (form != null) {
                    form.setOwnerGrid(BaseGrid.this);
                }
                callback.onSuccess(form);
            }
        });
    }

    @Override
    protected void createEditForm(DataInfoModel model, AsyncCallback<BaseForm> callback) {
        try {
            for (EditFormFactoryInfo formFactoryInfo : formFactories) {
                if (formFactoryInfo.getFormUsageCondition().isFormUsedForRow((Row) model)) {
                    callback.onSuccess(formFactoryInfo.getFormFactory().createForm());
                    return;
                }
            }
            callback.onSuccess(null);
        } catch (Throwable ex) {
            callback.onFailure(ex);
        }
    }

    public void addFormFactory(EditFormFactoryInfo factory) {
        formFactories.add(factory);
    }

    public boolean hasModifyForms() {
        if (formFactories.isEmpty()) {
            //считаем, что таблица еще не переведена на использование этого механизма
            return true;
        }
        for (EditFormFactoryInfo formFactoryInfo : formFactories) {
            if (formFactoryInfo.isInsertable()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isReadOnly(boolean deep) {
        if (!hasModifyForms()) {
            return true;
        }
        return super.isReadOnly(deep);
    }

    public void refreshChildGrids() {
        for (BaseForm form : openedForms) {
            form.refreshChildGrids();
        }
    }

    public OperationCommand createGridOperationCommand(OperationType type) {
        final DataInfoModel selectedItem = getGrid().getSelectionModel().getSelectedItem();
        final BigDecimal selectedRecord = selectedItem == null ? null : selectedItem.getRECORD_ID();
        final PageFilterInfo fetchParams = convertLoadConfig((FetchParams) getGrid().getLoader().getLastLoadConfig());
        fetchParams.setOffset(0);
        fetchParams.setLimit(0);

        final OperationCommand cmd = new OperationCommand(type);
        cmd.setGridContext(
                new GridParamsBean(
                        getGridContext(),
                        fetchParams,
                        getMarkedRecords(),
                        selectedRecord
                )
        );
        return cmd;
    }

    @Override
    public boolean onError(Throwable throwable) {
        return false;
    }

    @Override
    public boolean onServerError(ThrowableWrapper throwableWrapper) {
        return false;
    }

    public boolean getAllowSelection() {
        return ALLOW_SELECTION;
    }

    public void initGrid(GridMode[] flags) {
        GridModes helper = GridModes.build(flags);

        if (getAllowSelection() == true) {
            setMarksAllowed(helper.isMultiSelect());
        }

        forceFilter = helper.isForcedFilter();

        refreshAction = new GridRefreshAction(this);
        if (helper.isShowGridReload()) {
            reloadGridAction = new GridReloadAction(this);
        }

        if (!helper.isHideMove()) {

            moveFirstAction = new GridMoveFirstAction(this);
            moveLastAction = new GridMoveLastAction(this);
            moveNextAction = new GridMoveNextAction(this);
            movePrevAction = new GridMovePrevAction(this);
        }
        if (isMarksAllowed()) {
            markCheckAction = new GridMarkAction(this);
            markClearAction = new GridMarkClearAction(this);
            markInverseAllAction = new GridMarkInverseAllAction(this);

            moveFirstMarkAction = new GridMoveFirstMarkAction(this);
            moveLastMarkAction = new GridMoveLastMarkAction(this);
            moveNextMarkAction = new GridMoveNextMarkAction(this);
            movePrevMarkAction = new GridMovePrevMarkAction(this);
        }

        if (!helper.isHideInsert()) {
            insertAction = createInsertAction();
            if (helper.isAddClone()) {
                cloneAction = createCloneAction();
            }
        }

        if ((!helper.isHideUpdate()) && (!helper.isHideOnlyUpdate())) {
            updateAction = createUpdateAction();
            showAction = createShowAction();
            isOnlyShow = false;
        }
        if (!helper.isHideDelete()) {
            deleteAction = createDeleteAction();
        }
        if ((!helper.isHideUpdate()) && (helper.isHideOnlyUpdate())) {
            updateAction = new GridUpdateAction(this) {

                @Override
                public boolean checkEnabled() {
                    return false;
                }
            };
            showAction = createShowAction();
            isOnlyShow = true;
        }

        initEventMap();
    }

    protected GridDeleteAction createDeleteAction() {
        return new GridDeleteAction(this);
    }

    protected GridUpdateAction createUpdateAction() {
        return new GridUpdateAction(this);
    }

    protected GridShowAction createShowAction() {
        return new GridShowAction(this);
    }

    protected GridInsertAction createInsertAction() {
        return new GridInsertAction(this);
    }

    protected GridCloneAction createCloneAction() {
        return new GridCloneAction(this);
    }

    public void initEventMap() {
        bindEvent(GridEvent.SPACE_KEY_PRESS, getMarkCheckAction());
        bindEvent(GridEvent.DOUBLE_CLICK, getDblClickAction());
        bindEvent(GridEvent.DELETE_KEY_PRESS, getDeleteAction());
        bindEvent(GridEvent.INSERT_KEY_PRESS, getInsertAction());
        bindEvent(GridEvent.ENTER_KEY_PRESS, getUpdateAction());
    }

    public Action getMarkCheckAction() {
        return markCheckAction;
    }

    public Action getMarkClearAction() {
        return markClearAction;
    }

    public Action getMarkInvertAllAction() {
        return markInverseAllAction;
    }

    public Action getInsertAction() {
        return insertAction;
    }

    public Action getCloneAction() {
        return cloneAction;
    }

    public Action getUpdateAction() {
        return updateAction;
    }

    public Action getDblClickAction() {
        if (isOnlyShow) {
            return getShowAction();
        } else {
            return getUpdateAction();
        }

    }

    public Action getShowAction() {
        return showAction;
    }

    public Action getDeleteAction() {
        return deleteAction;
    }

    public Action getRefreshAction() {
        return refreshAction;
    }

    public Action getGridRloadAction() {
        return reloadGridAction;
    }

    public Action getMoveFirstAction() {
        return moveFirstAction;
    }

    public Action getMoveLastAction() {
        return moveLastAction;
    }

    public Action getMoveNextAction() {
        return moveNextAction;
    }

    public Action getMovePrevAction() {
        return movePrevAction;
    }

    public Action getMoveFirstMarkAction() {
        return moveFirstMarkAction;
    }

    public Action getMoveLastMarkAction() {
        return moveLastMarkAction;
    }

    public Action getMoveNextMarkAction() {
        return moveNextMarkAction;
    }

    public Action getMovePrevMarkAction() {
        return movePrevMarkAction;
    }

    public Action getExportAction() {
        return null;
    }

    public Event getLastRightClickEvent() {
        return lastRightClickEvent;
    }

    @Override
    protected void fireGridEvent(GridEvent event) {
        final Action action = actionsMap.get(event);
        if (action != null) {
            action.checkState();
            if (action.isEnabled()) {
                action.perform();
            }
        }
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public void setActionManager(ActionManager actionManager) {
        this.actionManager = actionManager;
    }

    @Override
    public void onUpdateState() {
        if (actionManager != null) {
            actionManager.updateState();
        }
    }

    public void setDefferedContextMenu(GridContextMenu gridContextMenu) {
        this.gridContextMenu = gridContextMenu;
        if (gridContextMenu == null) {
            setContextMenu(null);
        }
    }

    @Override
    protected void onRightClick(Event event) {
        if (event.getCtrlKey()) {
            return;
        }
        this.lastRightClickEvent = event;
        if (gridContextMenu != null && !contextMenuInitialized) {
            setContextMenu(gridContextMenu.getMenu());

            onUpdateState();
        }

        super.onRightClick(event);
    }

    @Override
    public void setContextMenu(Menu menu) {
        super.setContextMenu(menu);
        contextMenuInitialized = (menu != null);
    }

    @Override
    public void restoreFocus() {
        getGrid().getView().focus();
    }
}
