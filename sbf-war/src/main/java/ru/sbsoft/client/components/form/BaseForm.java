package ru.sbsoft.client.components.form;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.widget.core.client.*;
import ru.sbsoft.svc.widget.core.client.Dialog.PredefinedButton;
import ru.sbsoft.svc.widget.core.client.box.AlertMessageBox;
import ru.sbsoft.svc.widget.core.client.box.ConfirmMessageBox;
import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.container.Container;
import ru.sbsoft.svc.widget.core.client.container.VerticalLayoutContainer;
import ru.sbsoft.svc.widget.core.client.event.BeforeHideEvent;
import ru.sbsoft.svc.widget.core.client.event.DialogHideEvent;
import ru.sbsoft.svc.widget.core.client.event.HideEvent;
import ru.sbsoft.svc.widget.core.client.toolbar.SeparatorToolItem;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.BaseWindow;
import ru.sbsoft.client.components.IWindow.WindowHideHandler;
import ru.sbsoft.client.components.IWindow.WindowShowHandler;
import ru.sbsoft.client.components.actions.Action;
import ru.sbsoft.client.components.actions.ActionManager;
import ru.sbsoft.client.components.form.fields.Adapter;
import ru.sbsoft.client.components.form.fields.BaseAdapter;
import ru.sbsoft.client.components.form.fields.FormGridViewAdapter;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.i18n.GwTi18nUnit;
import ru.sbsoft.client.utils.*;
import ru.sbsoft.common.Strings;
import ru.sbsoft.sbf.app.ICondition;
import ru.sbsoft.sbf.app.Registration;
import ru.sbsoft.sbf.app.form.IHandler;
import ru.sbsoft.sbf.app.form.controller.DefaultContainerController;
import ru.sbsoft.sbf.app.form.controller.IFormContainerController.AfterSetValueEvent;
import ru.sbsoft.sbf.app.form.controller.IFormElementController;
import ru.sbsoft.sbf.app.form.controller.IFormSaveController;
import ru.sbsoft.sbf.app.form.controller.IFormSaveController.BeforeSaveEvent;
import ru.sbsoft.sbf.app.form.controller.IFormSaveController.PersistEvent;
import ru.sbsoft.sbf.app.form.controller.IFormSaveController.SaveErrorEvent;
import ru.sbsoft.sbf.app.form.controller.IFormSaveController.SaveEvent;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;
import ru.sbsoft.shared.api.i18n.consts.SBFFormStr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.consts.I18nType;
import ru.sbsoft.shared.exceptions.OptimisticLockException;
import ru.sbsoft.shared.exceptions.ThrowableWrapper;
import ru.sbsoft.shared.filter.FilterHelper;
import ru.sbsoft.shared.form.DeafultFormSaveController;
import ru.sbsoft.shared.interfaces.ConstraintType;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.services.IFormServiceAsync;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import ru.sbsoft.client.components.actions.event.KeyActionController;
import ru.sbsoft.client.components.actions.event.KeyUpDownDefinition;
import ru.sbsoft.client.components.actions.event.StdActKey;

/**
 * Базовый класс всех форм в приложении
 *
 * @param <MODEL>
 */
public abstract class BaseForm<MODEL extends IFormModel> {

    private final static Map<Window, BaseForm> windows = new HashMap<>();

    private final BaseWindow window = new BaseWindow();
    private final DefaultContainerController<MODEL> formController;
    private final BaseFormSaveController<MODEL> saveController;
    private IBaseFormOwner formOwner;

    private final GwTi18nUnit i18nUnit = GwTi18nUnit.getInstance();
    private IFormServiceAsync RPC = SBFConst.FORM_SERVICE;
    //
    private final ActionManager actionManager;
    //
    private FormMenu menu;
    private FormToolBar toolBar;
    private boolean actionAdded = false;
    private final TabPanel tabPanel = new PlainTabPanel();
    private final String caption;
    private String modcaption;
    //
    private boolean resetTabOnLoad = true;
    private boolean initTabsOnSave = true;
    //
    private IFormSaveController.FormSaveBehvior<MODEL> saveBehavior;
    private final FormChangesControl changesControl = new FormChangesControl();
    private final Map<Widget, BaseAdapter> fields = new HashMap<>();
    private List<ConstraintType> uniqConstraints;
    //
    private List<FilterInfo> newModelInitParams;
    //
    private final KeyActionController keyActionController;

    private boolean forceClose = false;
    private final Runnable CLOSE_CALLBACK = new Runnable() {
        @Override
        public void run() {
            close();
        }
    };
    //
    private final WindowAsyncCallback<MODEL> showFormCallback = new WindowAsyncCallback<MODEL>(window) {

        @Override
        public void onResult(MODEL result) {
            if (!isVisible()) {
                getWindow().setVisible(true);
            }
            if (resetTabOnLoad) {
                selectFirstPage();
            }
            setModel(result);
        }

        private void selectFirstPage() {
            if (getTabPanel().getWidgetCount() > 0) {
                getTabPanel().setActiveWidget(getTabPanel().getWidget(0));
            }
        }
    };

    public BaseForm(final String caption) {
        this(caption, true);
    }

    public void setFormService(IFormServiceAsync formService) {
        this.RPC = formService;
    }

    private void addRegion(final Component component, VerticalLayoutContainer.VerticalLayoutData layout) {

        if (component != null) {
            window.addRegion(component, layout);
        }
    }

    protected void initBars(BaseWindow window) {
        addRegion(toolBar = createFormToolBar(), VLC.CONST);

    }

    public BaseForm(final String caption, final boolean showMenu) {
        super();
        windows.put(window.getWindow(), this);
        this.caption = caption;

        actionManager = new ActionManager();

        tabPanel.setTabScroll(true);
        tabPanel.setBodyBorder(false);
        XElement.as(tabPanel.getElement().getChild(0)).getStyle().setProperty("borderWidth", "0");

        formController = new DefaultContainerController<>();
        saveController = new BaseFormSaveController<>();
        if (showMenu) {

            addRegion(menu = createFormMenu(), VLC.CONST);
        }
        initBars(window);
        addRegion(tabPanel, VLC.FILL);

        //window.
        getWindow().addBeforeHideHandler(new BeforeHideEvent.BeforeHideHandler() {
            @Override
            public void onBeforeHide(BeforeHideEvent event) {
                event.setCancelled(!checkSaveChanges(CLOSE_CALLBACK));
//                if (!event.isCancelled()) {
//                    if (isGridInitialized()) {
//                        if (formOwner != null) {
//                            formOwner.restoreFocus();
//                        }
//                    }
//                }
            }
        });
        getWindow().addHideHandler(new HideEvent.HideHandler() {
            @Override
            public void onHide(HideEvent event) {
                if (formOwner != null) {
                    Scheduler.get().scheduleDeferred(() -> formOwner.restoreFocus());
                }
            }
        });

        setWidth(500);
        getWindow().getHeader().setIcon(SBFResources.APP_ICONS.Form());

        window.addAfterFirstAttachListener(new BaseWindow.AfterFirstAttachHandler() {
            @Override
            public void onAfterFirstAttach() {
                BaseForm.this.onFinishingInitialization();
                BaseForm.this.onAfterFirstAttach();
                getTabPanel().addSelectionHandler(new SelectionHandler<Widget>() {
                    @Override
                    public void onSelection(SelectionEvent<Widget> event) {
                        final Widget widget = event.getSelectedItem();
                        if (widget instanceof SimplePageFormContainer) {
                            refreshFormGridViews((SimplePageFormContainer) widget);
                        } else if (widget instanceof FormGridView) {
                            ((FormGridView) widget).refresh();
                        }
                    }
                });
            }
        });

        keyActionController = new KeyActionController(window.getWindow());
        keyActionController.addAction(StdActKey.SAVE, () -> execBt(toolBar.getSaveRefreshButton()));
        keyActionController.addAction(StdActKey.APPLY, () -> execBt(toolBar.getSaveButton()));
        keyActionController.addAction(StdActKey.QUIT, () -> execBt(toolBar.getCloseButton()));
        keyActionController.addAction(StdActKey.REFRESH, () -> execBt(toolBar.getRefreshButton()));
        keyActionController.addAction(StdActKey.SWITCH_TAB, () -> CliUtil.switchNextTab(tabPanel));

    }

    private void execBt(TextButton bt) {
        if (bt.isEnabled()) {
            ActionManager.execAttachedAction(bt);
        }
    }

    public void addConfirmation(Function<MODEL, Boolean> check, String msg) {
        saveController.addConfirmation(check, msg);
    }

    public Registration addSaveHandler(IHandler<SaveEvent<MODEL>> handler) {
        return saveController.addSaveHandler(handler);
    }

    public Registration addPersistHandler(IHandler<PersistEvent<MODEL>> handler) {
        return saveController.addPersistHandler(handler);
    }

    public Registration addEditHandler(IHandler<IFormSaveController.EditEvent<MODEL>> handler) {
        return saveController.addEditHandler(handler);
    }

    public Registration addSaveErrorHandler(IHandler<IFormSaveController.SaveErrorEvent<MODEL>> handler) {
        return saveController.addSaveErrorHandler(handler);
    }

    public Registration addReadOnlyCondition(ICondition readOnlyCondition) {
        return formController.addReadOnlyCondition(readOnlyCondition);
    }

    public void addFormElement(IFormElementController<MODEL> formElement) {
        formController.addFormElementController(formElement);
    }

    public void removeFormElement(IFormElementController<MODEL> formElement) {
        formController.removeFormElementController(formElement);
    }

    private TabPanel getTabPanel() {
        return tabPanel;
    }

    public Window getWindow() {
        return window.getWindow();
    }

    public String getModcaption() {
        return modcaption;
    }

    public void setModcaption(String modcaption) {
        this.modcaption = modcaption;
    }

    public Map<Widget, BaseAdapter> getFields() {
        return fields;
    }

    public FormToolBar getToolBar() {
        return toolBar;
    }

    protected IFormSaveController.FormSaveBehvior<MODEL> createSaveBehvior() {
        return new IFormSaveController.FormSaveBehvior<MODEL>() {
            @Override
            public void save(final MODEL model, final IFormSaveController.FormSaveCallback<MODEL> callback) {
                saveEditors(new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(Void result) {
                        saveModel(model, callback);
                    }
                });
            }

            private void saveEditors(final AsyncCallback<Void> callback) {
                final List<IEditorComponent> editors = fields.keySet().stream().filter(w -> w instanceof IEditorComponent).map(w -> (IEditorComponent) w).collect(Collectors.toList());
                AsyncCallback<Void> saver = new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(Void result) {
                        if (!editors.isEmpty()) {
                            IEditorComponent ed = editors.remove(editors.size() - 1);
                            ed.save(this);
                        } else {
                            callback.onSuccess(result);
                        }
                    }
                };
                saver.onSuccess(null);
            }

            private void saveModel(final MODEL model, final IFormSaveController.FormSaveCallback<MODEL> callback) {
                final DefaultAsyncCallback<MODEL> defaultAsyncCallback = new DefaultAsyncCallback<MODEL>(window.getWindow()) {
                    @Override
                    public void onResult(MODEL result) {
                        callback.onResult(result);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        final boolean handled = callback.onFailure(caught);
                        if (!handled) {
                            super.onFailure(caught);
                        }
                    }
                };
                defaultAsyncCallback.setErrorHandler(new ClientUtils.ErrorHandler() {
                    @Override
                    public boolean onError(Throwable throwable) {
                        return BaseForm.this.onError(throwable);
                    }

                    @Override
                    public boolean onServerError(ThrowableWrapper throwableWrapper) {
                        return BaseForm.this.onServerError(throwableWrapper);
                    }
                });
                RPC.putRecord(getFormContext(), model, formOwner == null ? null : formOwner.getParentFilters(), defaultAsyncCallback);
            }
        };
    }

    protected FormMenu createFormMenu() {
        return new FormMenu(this);
    }

    protected FormToolBar createFormToolBar() {
        return new FormToolBar(this);
    }

    private void updateLabels() {
        for (int i = 0; i < getTabPanel().getWidgetCount(); i++) {
            updateLabels(getTabPanel().getWidget(i));
        }
    }

    private void updateLabels(Widget w) {
        if (w != null) {
            if (w instanceof SimplePageFormContainer) {
                ((SimplePageFormContainer) w).updateLabels();
            } else if (w instanceof Container) {
                Container c = (Container) w;
                for (int i = 0; i < c.getWidgetCount(); i++) {
                    updateLabels(c.getWidget(i));
                }
            }
        }
    }

    private void onFinishingInitialization() {

        createEditors(getTabPanel());
        updateLabels();

        updateFieldsInfo(getTabPanel());

        changesControl.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                actionManager.updateState();
            }
        });

        saveBehavior = createSaveBehvior();

        saveController.addBeforeSaveHandler(new IHandler<BeforeSaveEvent<MODEL>>() {
            @Override
            public void onHandle(BeforeSaveEvent<MODEL> event) {
                tabPanel.mask(I18n.get(SBFGeneralStr.saveData));
                if (null != getOwnerGrid()) {
                    getOwnerGrid().setWasChanged(true);
                }
            }
        });

        saveController.addSaveHandler(new IHandler<SaveEvent<MODEL>>() {
            @Override
            public void onHandle(SaveEvent<MODEL> event) {
                tabPanel.unmask();
                formController.writeFrom(event.getModel(), true);
            }
        });

        saveController.addSaveErrorHandler(new IHandler<SaveErrorEvent<MODEL>>() {
            @Override
            public void onHandle(SaveErrorEvent event) {
                tabPanel.unmask();
                final Throwable caught = event.getCaught();
                if (caught instanceof OptimisticLockException) {
                    final String errorTitle = i18nUnit.i18n(I18nType.FORM, "msgOptimiscticLockTitle");
                    final String errorText = i18nUnit.i18n(I18nType.FORM, "msgOptimiscticLockError");
                    new AlertMessageBox(errorTitle, errorText).show();
                    //unmask();
                    event.setHandled(true);
                }
            }
        });

        formController.addReadOnlyCondition(new ICondition() {
            @Override
            public boolean check() {
                if (isFormReadOnlyForUser()) {
                    return true;
                }
                final IFormModel model = formController.getValue();
                return model != null && model.isReadOnly();

            }
        });

        formController.addAfterSetValueHandler(new IHandler<AfterSetValueEvent<MODEL>>() {
            @Override
            public void onHandle(AfterSetValueEvent<MODEL> event) {
                final MODEL model = event.getModel();
                dataToForm(model);
                setChanged(false);
                setHeading(makeCaption());

                if (formOwner != null && formOwner.isReadOnly(true)) {
                    formController.setReadOnly(true);
                }

                updateState(true);
                updateGridsState();
                refreshChildGrids();
                refreshGridRow();

                updateChildReadOnly(formController.isReadOnly());
            }
        });

    }

    protected void onAfterFirstAttach() {
    }

    public void refreshFormGridViews(SimplePageFormContainer container) {
        for (BaseAdapter adapter : fields.values()) {
            if (adapter instanceof FormGridViewAdapter) {
                if (adapter.getHolder() == container) {
                    ((FormGridViewAdapter) adapter).getFormGridView().refresh();
                }
            }
        }
    }

    private void saveData(final Runnable callback) {
        final Registration[] saveHandlerRegistration = new Registration[1];
        final MODEL oldModel = formController.getValue();
        formToData(oldModel);

        saveHandlerRegistration[0] = saveController.addSaveHandler(new IHandler<SaveEvent<MODEL>>() {
            @Override
            public void onHandle(SaveEvent<MODEL> event) {
                final MODEL newModel = event.getModel();
                onGetRow(oldModel, newModel, callback);
                if (saveHandlerRegistration[0] != null) {
                    saveHandlerRegistration[0].remove();
                }
            }
        });

        saveController.save(formController.getValue(), saveBehavior);
    }

    protected void onGetRow(MODEL oldModel, MODEL newModel, Runnable callback) {

        setModel(newModel, false);

        callback(callback);
    }

    public void setModel(MODEL value) {
        setModel(value, true);
    }

    public void setModel(final MODEL model, final boolean toUpdateRowInGrid) {
        formController.writeFrom(model, toUpdateRowInGrid);
        setTemporaryReadOnly(formController.isReadOnly());
    }

    private void refreshGridRow() {
        if (formOwner == null) {
            return;
        }

        if (!isAppendMode() && isGridInitialized()) {
            formOwner.refreshRow(formController.getValue());
        }
    }

    private boolean isGridInitialized() {
        return formOwner != null && formOwner.isInitialized();
    }

    private String makeCaption() {
        final String div = " - ";

        final boolean isSpec = getModcaption() != null ? getModcaption().endsWith("+") : false;
        final String modcap = getModcaption();

        StringBuilder builder = new StringBuilder(modcap == null ? caption : (isSpec ? modcap.substring(0, modcap.length() - 1) : modcap));
        IFormModel dataModel = formController.getValue();
        if (dataModel != null && !isSpec) {
            builder.append(div);
            if (dataModel.getId() == null) {
                builder.append(I18n.get(SBFEditorStr.captionNew));
            } else {
                String uiName = dataModel.getUiName();
                if (uiName != null && (uiName = uiName.trim()).isEmpty()) {
                    uiName = null;
                }
                if (isReadOnly()) {
                    String captView = I18n.get(SBFFormStr.captView);
                    if (uiName != null) {
                        builder.append(captView.toUpperCase());
                        builder.append(div).append(uiName);
                    } else {
                        builder.append(captView);

                    }
                } else {
                    if (uiName != null) {
                        builder.append(uiName);
                    } else {
                        builder.append(I18n.get(SBFEditorStr.captionUpdate));
                    }
                }
            }
        }
        return builder.toString();
    }

    public void setOwnerGrid(BaseGrid ownerGrid) {
        this.formOwner = ownerGrid;
    }

    public void setOwner(IBaseFormOwner owner) {
        this.formOwner = owner;
    }

    public void finishEditing() {
        if (isReadOnly()) {
            return;
        }
        for (Adapter adapter : fields.values()) {
            adapter.finishEditing();
        }
    }

    public void clearInvalid() {
        for (Adapter adapter : fields.values()) {
            adapter.clearInvalid();
        }
    }

    public void setNewModelInitParams(List<ParamInfo> params) {
        newModelInitParams = params != null && !params.isEmpty() ? FilterHelper.toFilters(params) : null;
    }

    private void add(Map<String, FilterInfo> filterMap, List<FilterInfo> filterList) {
        if (filterList != null) {
            for (FilterInfo f : filterList) {
                filterMap.put(f.getColumnName(), f);
            }
        }
    }

    public void show(BigDecimal recordID) {
        Map<String, FilterInfo> mix = new HashMap<String, FilterInfo>();
        if (formOwner != null) {
            add(mix, formOwner.getParentFilters());
            add(mix, formOwner.getDefinedFilters());
        }

        if (null == recordID) {
            add(mix, newModelInitParams);
            RPC.newRecord(getFormContext(), new ArrayList<FilterInfo>(mix.values()), formOwner == null ? null : formOwner.getClonableRecordID(), showFormCallback);
        } else {
            RPC.getRecord(getFormContext(), recordID, new ArrayList<FilterInfo>(mix.values()), showFormCallback);
        }

        if (isVisible()) {
            mask((null == recordID) ? I18n.get(SBFGeneralStr.newData)
                    : I18n.get(SBFGeneralStr.loadData));
        }
    }

    public void showNewWithParams(final List<ParamInfo> params) {
        final List<FilterInfo> filters = new ArrayList<FilterInfo>();

        if (formOwner != null) {
            filters.addAll(formOwner.getParentFilters());
        }
        if (null != params) {
            for (final ParamInfo parameter : params) {
                filters.add(FilterHelper.createFilterInfo(parameter));
            }
        }
        RPC.newRecord(getFormContext(), filters, formOwner == null ? null : formOwner.getClonableRecordID(), showFormCallback);

        if (isVisible()) {
            mask(I18n.get(SBFGeneralStr.newData));
        }
    }

    protected void close() {
        window.setVisible(false);
    }

    public void hide() {
        close();
    }

    public void updateState(boolean forceUpdate) {
        actionManager.updateState();
    }

    private boolean isFormReadOnlyForUser() {
        return RoleCheker.getInstance().isTableReadOnly(getSecurityId());
    }

    public boolean isReadOnly() {
        return formController.isReadOnly();
    }

    public BaseForm setReadOnly(boolean readOnly) {
        formController.setReadOnly(readOnly);
        setTemporaryReadOnly(readOnly);
        return this;
    }

    public String getCaption() {
        return caption;
    }

    public boolean isChanged() {
        finishEditing();
        return changesControl.isChanged();
    }

    public void setChanged(boolean changed) {
        changesControl.setChanged(changed);
    }

    public void saveClose() {
        save(CLOSE_CALLBACK);
    }

    public void saveRefresh() {
        save(null);
    }

    public void save(final Runnable callback) {
        initAllTabs(new Runnable() {

            @Override
            public void run() {
                if (isChanged()) {
                    if (validate()) {
                        saveData(callback);
                    }
                } else {
                    callback(callback);
                }
            }
        });
    }

    public void persist() {
        if (!isAppendMode()) {
            throw new IllegalStateException();
        }
        saveData(null);
    }

    private void initAllTabs(final Runnable callback) {
        if (!initTabsOnSave) {
            callback.run();
            return;
        }

        final LazyPageForm page = findNotInitalizesLazyPage();
        if (page == null) {
            callback.run();
            return;
        }

        final Widget activeWidget = getTabPanel().getActiveWidget();
        getTabPanel().setAnimScroll(false);
        Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {

            @Override
            public boolean execute() {
                final LazyPageForm lazyPage = findNotInitalizesLazyPage();
                if (lazyPage != null) {
                    getTabPanel().setActiveWidget(lazyPage);
                    getTabPanel().setActiveWidget(activeWidget);
                    mask(I18n.get(SBFFormStr.msgValidationData) + " " + getTabPanel().getConfig(lazyPage).getText());
                    return true;
                } else {
                    getTabPanel().setActiveWidget(activeWidget);
                    getTabPanel().scrollToTab(activeWidget, false);
                    unmask();
                    getTabPanel().setAnimScroll(true);
                    callback.run();
                    return false;
                }
            }
        }, 10);
    }

    private LazyPageForm findNotInitalizesLazyPage() {
        for (int i = 0; i < getTabPanel().getWidgetCount(); i++) {
            final Widget w = getTabPanel().getWidget(i);
            if (w instanceof LazyPageForm) {
                final LazyPageForm name = (LazyPageForm) w;
                if (!name.isInitalized()) {
                    return name;
                }
            }
        }
        return null;
    }

    protected static void callback(Runnable runnable) {
        if (runnable != null) {
            runnable.run();
        }
    }

    protected boolean validate() {
        finishEditing();
        for (Adapter adapter : fields.values()) {
            if (!adapter.isValid()) {
                alert(adapter.getWidget());
                return false;
            }
        }
        //
        String customValidateMessage = getCustomValidateMessage(fields);
        if (customValidateMessage != null) {
            (new AlertMessageBox(I18n.get(SBFGeneralStr.captQuery), customValidateMessage)).show();
            return false;
        }
        return true;
    }

    public String getCustomValidateMessage(Map<Widget, BaseAdapter> fields) {
        return null;
    }

    protected void alert(Widget f) {
        selectActiveTab(f);
    }

    protected void selectActiveTab(Widget w) {
        while ((w = w.getParent()) != null) {
            if (w.getParent() == getTabPanel().getContainer()) {
                getTabPanel().setActiveWidget(w);
                return;
            }
        }
    }

    public void show() {
        window.setVisible(true);
    }

    private class ConfirmSaveBox extends ConfirmMessageBox {

        public ConfirmSaveBox() {
            super(I18n.get(SBFGeneralStr.captQuery), I18n.get(SBFGeneralStr.msgModify));
            super.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO, PredefinedButton.CANCEL);
            final KeyActionController actCtrl = new KeyActionController(this);
            actCtrl.addAction(StdActKey.YES, () -> onButtonPressed(getButton(PredefinedButton.YES)));
            actCtrl.addAction(StdActKey.NO, () -> onButtonPressed(getButton(PredefinedButton.NO)));
            actCtrl.addAction(Arrays.asList(new KeyUpDownDefinition(KeyCodes.KEY_ESCAPE), StdActKey.create(KeyCodes.KEY_C)), () -> onButtonPressed(getButton(PredefinedButton.CANCEL)));
        }
    }

    public boolean checkSaveChanges(final Runnable callback) {
        updateFieldsInfo(getTabPanel());
        actionManager.updateState();
        if (!isChanged() || forceClose) {
            forceClose = false;
            return true;
        } else {
            final ConfirmMessageBox box = new ConfirmSaveBox();
            box.addDialogHideHandler(new DialogHideEvent.DialogHideHandler() {
                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == PredefinedButton.YES) {
                        save(callback);
                    } else if (event.getHideButton() == PredefinedButton.NO) {
                        changesControl.setChanged(false);
                        forceClose = true;
                        callback(callback);
                    }
                }
            });
            box.show();
            return false;
        }
    }

    public void refresh() {
        final MODEL model = formController.getValue();
        if (model != null) {
            finishEditing();
            if (model.getId() != null) {
                show(model.getId());
            }
        }
    }

    public void updateFieldsInfo(final TabPanel tabPanel) {
        for (int i = 0; i < tabPanel.getWidgetCount(); i++) {
            final Widget w = tabPanel.getWidget(i);
            if (w instanceof HasWidgets) {
                updateFieldsInfo((HasWidgets) w);
            }
        }
    }

    public void updateFieldsInfo(HasWidgets hasWidgets) {
        registrFields(hasWidgets, hasWidgets);
    }

    private void registrFields(HasWidgets hasWidgets, HasWidgets mainControl) {
        for (Widget w : hasWidgets) {
            if (w instanceof IFormAutonomousComponent) {
                continue;
            }
            if (w instanceof FormGridView) {
                final FormGridView fgw = (FormGridView) w;
                if (fgw.getGrid() instanceof IEditorComponent) {
                    w = fgw.getGrid();
                }
            }
            if (fields.containsKey(w)) {
                continue;
            }
            final BaseAdapter adapter = BaseAdapter.create(w, mainControl);
            if (adapter != null) {
                adapter.registr(changesControl);
                fields.put(w, adapter);
                continue;
            }
            if (w instanceof HasWidgets) {
                registrFields((HasWidgets) w, mainControl);
            }
        }
    }

    private void updateChildReadOnly(boolean readOnly) {
        for (BaseAdapter adapter : fields.values()) {
            adapter.setReadOnly(readOnly);
        }
    }

    @Deprecated
    private void setTemporaryReadOnly(boolean readOnly) {
        for (BaseAdapter adapter : fields.values()) {
            adapter.setTemporaryReadOnly(readOnly);
        }
    }

    public void setReadOnly(Component component, boolean readOnly) {
        findAdapter(component).setTemporaryReadOnly(readOnly);
    }

    public void setReadOnlyGroup(HasWidgets holder, boolean readOnly) {
        if (holder == null) {
            throw new IllegalArgumentException("holder can't be null");
        }
        for (BaseAdapter adapter : fields.values()) {
            if (adapter.getHolder() == holder) {
                adapter.setTemporaryReadOnly(readOnly);
            }
        }
    }

    public void updateReadOnly(HasWidgets holder, boolean readOnly) {
        if (holder == null) {
            throw new IllegalArgumentException("holder can't be null");
        }
        for (BaseAdapter adapter : fields.values()) {
            if (adapter.getHolder() == holder) {
                adapter.setReadOnly(readOnly);
            }
        }
    }

    protected BaseAdapter findAdapter(Component component) {
        if (component instanceof TextFieldSet) {
            component = ((TextFieldSet) component).getFieldText();

        }
        final BaseAdapter adapter = fields.get(component);
        if (adapter == null) {
            throw new IllegalStateException("not found " + component.getClass().getName());
        }
        return adapter;
    }

    protected void updateGridsState() {
        for (Adapter adapter : fields.values()) {
            if (adapter instanceof FormGridViewAdapter) {
                adapter.setReadOnly(mustAdapterBeReadOnly());
            }
        }
    }

    protected void updateEditorsState() {
        for (Adapter adapter : fields.values()) {
            adapter.setReadOnly((adapter instanceof FormGridViewAdapter) ? mustAdapterBeReadOnly() : isReadOnly());
        }
    }

    private boolean mustAdapterBeReadOnly() {
        return formController.isReadOnly() || isAppendMode();
    }

    public BigDecimal getRecordUQ() {
        return formController.getValue() == null ? null : formController.getValue().getId();
    }

    public MODEL getDataModel() {
        return formController.getValue();
    }

    public String getContextName(String context) {
        return context;
        // return context + ((null == dataModel || null == dataModel.getID()) ? "" : dataModel.getID().toString());
    }

    protected abstract void createEditors(final TabPanel tabEditors);

    protected abstract void formToData(final MODEL dataModel);

    protected abstract void dataToForm(final MODEL dataModel);

    protected abstract String getSecurityId();

    protected abstract FormContext getFormContext();

    public BaseGrid getOwnerGrid() {
        return formOwner instanceof BaseGrid ? (BaseGrid) formOwner : null;
    }

    public IBaseFormOwner getFormOwner() {
        return formOwner;
    }

    public TabPanel getMainTab() {
        return getTabPanel();
    }

    public void addAction(Action action) {
        if (!actionAdded) {
            actionAdded = true;

            if (toolBar != null) {
                toolBar.add(new SeparatorToolItem());
            }
        }
        if (menu != null) {
            menu.getOperationsMenu().addAction(action);
        }
        if (toolBar != null) {
            toolBar.addButton(action);
        }
    }

    public boolean onError(Throwable throwable) {
        return false;
    }

    public boolean onServerError(ThrowableWrapper t) {
        if (uniqConstraints == null) {
            return false;
        }
        while (t != null) {
            final String c = t.getClassName();
            final String m = ClientUtils.convertMessage(t);
            if (c != null && m != null) {
                if (c.contains("SQLIntegrityConstraintViolationException")) {
                    String title = getUniqueConstraintTitle(m);
                    if (title != null) {
                        ClientUtils.showError(Strings.formatEasy(I18n.get(SBFGeneralStr.msgViolatedUnique), title));
                    }
                    return (title != null);
                }
            }
            t = t.getCause();
        }
        return false;
    }

    @Deprecated
    public void addUniqConstraintInfo(final ConstraintType type) {
        if (uniqConstraints == null) {
            uniqConstraints = new ArrayList<ConstraintType>();
        }
        uniqConstraints.add(type);
    }

    private String getUniqueConstraintTitle(String message) {
        for (ConstraintType type : uniqConstraints) {
            if (message.contains(type.getCode())) {
                return type.getTitle();
            }
        }
        return null;
    }

    public boolean isAppendMode() {
        return formController.getValue() == null || formController.getValue().getId() == null;
    }

    public void refreshChildGrids() {
        for (Adapter adapter : fields.values()) {
            if (adapter instanceof FormGridViewAdapter) {
                ((FormGridViewAdapter) adapter).getFormGridView().getGrid().tryReload();
            }
        }
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public void view(BigDecimal recordID) {
        if (menu != null) {
            menu.remove(1);
        }
        show(recordID);
    }

    public boolean isResetTabOnLoad() {
        return resetTabOnLoad;
    }

    public void setResetTabOnLoad(boolean resetTabOnLoad) {
        this.resetTabOnLoad = resetTabOnLoad;
    }

    public boolean isInitTabsOnSave() {
        return initTabsOnSave;
    }

    public void setInitTabsOnSave(boolean initTabsOnSave) {
        this.initTabsOnSave = initTabsOnSave;
    }

    public MODEL getModel() {
        return formController.getValue();
    }

    public void setHeading(String headingText) {
        window.setHeading(SafeHtmlUtils.fromString(headingText));
    }

    public void setHeading(SafeHtml header) {
        window.setHeading(header);
    }

    public void setWidth(int width) {
        window.setWidth(width);
    }

    public void setMinWidth(int minWidth) {
        window.setMinWidth(minWidth);
    }

    public void setHeight(int height) {
        window.setHeight(height);
    }

    public void setMinHeight(int minHeight) {
        window.setMinHeight(minHeight);
    }

    public void setResizable(boolean resisable) {
        window.setResizable(resisable);
    }

    public int getLeft() {
        return window.getLeft();
    }

    public int getTop() {
        return window.getTop();
    }

    public void mask(String message) {
        window.mask(message);
    }

    public void mask() {
        window.mask();
    }

    public void unmask() {
        window.unmask();
    }

    public boolean isVisible() {
        return window.isVisible();
    }

    public void setPixelSize(int width, int height) {
        window.setPixelSize(width, height);
    }

    public void setModal(boolean modal) {
        window.setModal(modal);
    }

    public boolean isModal() {
        return window.isModal();
    }

    public void setClosable(boolean closable) {
        window.setClosable(closable);
    }

    public void setCollapsible(boolean collapsible) {
        window.setCollapsible(collapsible);
    }

    public void setMaximizable(boolean maximizable) {
        window.setMaximizable(maximizable);
    }

    public void setVisible(boolean visible) {
        window.setVisible(visible);
    }

    public Registration addHideHandler(WindowHideHandler handler) {
        return window.addHideHandler(handler);
    }

    public Registration addShowHandler(WindowShowHandler handler) {
        return window.addShowHandler(handler);
    }

    public static BaseForm forSvcWindow(Window w) {
        return windows.get(w);
    }

    public void updateView() {
        SbfFieldHelper.updateLabelsAlign(getMainTab().getContainer());
        CliUtil.twitchWindowWidth(getWindow());
    }

    private static class BaseFormSaveController<MODEL extends IFormModel> extends DeafultFormSaveController<MODEL> {

        private Confirmation confirmation = null;

        public void addConfirmation(Function<MODEL, Boolean> check, String msg) {
            Confirmation c = new Confirmation(check, msg);
            Confirmation last = getLastConfirmation();
            if (last != null) {
                last.setNext(c);
            } else {
                confirmation = c;
            }
        }

        private Confirmation getLastConfirmation() {
            if (null == confirmation) {
                return null;
            }
            Confirmation con = confirmation;
            while (con.next != null) {
                con = con.next;
            }
            return con;
        }

        @Override
        public void save(final MODEL model, final FormSaveBehvior saveBehavior) {
            if (confirmation == null) {
                super.save(model, saveBehavior);
            } else {
                confirmation.confirm(model, m -> super.save(m, saveBehavior));
            }
        }

        private class Confirmation {

            private final Function<MODEL, Boolean> check;
            private final String msg;
            private Confirmation next = null;

            public Confirmation(Function<MODEL, Boolean> check, String msg) {
                this.check = check;
                this.msg = msg;
            }

            public Function<MODEL, Boolean> getCheck() {
                return check;
            }

            public String getMsg() {
                return msg;
            }

            public void setNext(Confirmation next) {
                this.next = next;
            }

            public void confirm(MODEL m, Consumer<MODEL> c) {
                if (Boolean.TRUE.equals(check.apply(m))) {
                    showConfirm(b -> {
                        if (Boolean.TRUE.equals(b)) {
                            callNext(m, c);
                        }
                    });
                } else {
                    callNext(m, c);
                }
            }

            private void callNext(MODEL m, Consumer<MODEL> c) {
                if (next != null) {
                    next.confirm(m, c);
                } else {
                    c.accept(m);
                }
            }

            private void showConfirm(Consumer<Boolean> res) {
                ConfirmMessageBox box = new ConfirmMessageBox(I18n.get(SBFGeneralStr.captQuery), msg);
                box.setPredefinedButtons(Dialog.PredefinedButton.YES, Dialog.PredefinedButton.NO);
                box.addDialogHideHandler((DialogHideEvent event) -> {
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        res.accept(Boolean.TRUE);
                    } else {
                        res.accept(Boolean.FALSE);
                    }
                });
                box.setModal(true);
                box.show();
            }
        }
    }
}
