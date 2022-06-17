package ru.sbsoft.client.components.form;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.cell.core.client.SimpleSafeHtmlCell;
import ru.sbsoft.svc.cell.core.client.form.ComboBoxCell;
import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.svc.data.shared.LabelProvider;
import ru.sbsoft.svc.data.shared.ListStore;
import ru.sbsoft.svc.data.shared.ModelKeyProvider;
import ru.sbsoft.svc.data.shared.PropertyAccess;
import ru.sbsoft.svc.data.shared.SortDir;
import ru.sbsoft.svc.data.shared.Store;
import ru.sbsoft.svc.data.shared.event.StoreAddEvent;
import ru.sbsoft.svc.widget.core.client.ContentPanel;
import ru.sbsoft.svc.widget.core.client.TabPanel;
import ru.sbsoft.svc.widget.core.client.container.VerticalLayoutContainer;
import ru.sbsoft.svc.widget.core.client.event.CellClickEvent;
import ru.sbsoft.svc.widget.core.client.event.RefreshEvent;
import ru.sbsoft.svc.widget.core.client.form.FieldLabel;
import ru.sbsoft.svc.widget.core.client.form.TextField;
import ru.sbsoft.svc.widget.core.client.grid.CellSelectionModel;
import ru.sbsoft.svc.widget.core.client.grid.ColumnConfig;
import ru.sbsoft.svc.widget.core.client.grid.Grid;
import ru.sbsoft.svc.widget.core.client.grid.editing.GridInlineEditing;
import ru.sbsoft.svc.widget.core.client.form.ComboBox;
import ru.sbsoft.svc.widget.core.client.grid.ColumnModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.StoreDataMutationEvent;
import ru.sbsoft.client.components.StoreDataMutationHandler;
import ru.sbsoft.client.components.actions.AbstractAction;
import ru.sbsoft.client.components.actions.ActionManager;
import ru.sbsoft.client.components.form.fields.BaseAdapter;
import ru.sbsoft.client.components.form.handler.form.CheckHandler;
import ru.sbsoft.client.components.form.handler.form.TextAreaHandler;
import ru.sbsoft.client.components.form.handler.form.TextHandler;
import ru.sbsoft.client.components.form.handler.form.builder.FSet;
import ru.sbsoft.client.components.form.handler.form.builder.IFaceBuilder;
import ru.sbsoft.client.components.form.handler.form.builder.Tab;
import ru.sbsoft.client.components.form.model.ITehnologyModelAccessor;
import ru.sbsoft.client.components.grid.CustomToolBar;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.VLC;
import ru.sbsoft.sbf.svc.components.VerticalFieldSet;
import ru.sbsoft.shared.NamedFormContext;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;
import ru.sbsoft.shared.api.i18n.consts.SBFReportStr;
import ru.sbsoft.shared.consts.CustomReportParamType;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.model.CustomReportFilterInfo;
import ru.sbsoft.shared.model.CustomReportModel;
import ru.sbsoft.shared.model.CustomReportParamModel;
import ru.sbsoft.shared.param.StringParamInfo;

/**
 * Форма редактирования пользовательского отчета
 *
 * @author sokolov
 */
public class CustomReportFormFactory extends BaseFormFactory<CustomReportModel, Row> {

    interface CustomReportAccess extends ITehnologyModelAccessor<CustomReportModel> {

        ValueProvider<CustomReportModel, String> reportName();

        ValueProvider<CustomReportModel, String> reportPath();

        ValueProvider<CustomReportModel, Boolean> includeIdRow();
        
        ValueProvider<CustomReportModel, String> headerSQL();

    }

    public CustomReportFormFactory(NamedFormContext ctx) {
        super(ctx);
        setWidth(800);
    }

    @Override
    protected BaseFactoryForm createFormInstance() {
        return new CustomReportFormImpl();
    }

    private class CustomReportFormImpl extends BaseFactoryForm {

        private static final int LABEL_WIDTH = 220;

        private ActionManager actionManager;
        private Scheduler.ScheduledCommand cmd = null;
        private ListStore<CustomReportParamModel> paramEditStore;
        private BigDecimal maxId = BigDecimal.ZERO;
        private VerticalLayoutContainer vlc;

        private FilterSet filterSet;

        public CustomReportFormImpl() {
            super();
            setHeight(650);
        }

        @Override
        protected void buildFace(IFaceBuilder<CustomReportModel> b) {
            super.buildFace(b);
            CustomReportAccess a = GWT.create(CustomReportAccess.class);
            b.add(new Tab<CustomReportModel>(I18n.get(SBFReportStr.titleFormReportSet), LABEL_WIDTH)
                    .add(new FSet<CustomReportModel>(I18n.get(SBFReportStr.titleFormReportSet))
                            .add(new TextHandler<CustomReportModel>(I18n.get(SBFReportStr.titleFormFieldName), a.reportName()).setReq().setToolTip(I18n.get(SBFReportStr.hintFormFieldName)))
                            .add(new TextHandler<CustomReportModel>(I18n.get(SBFReportStr.titleFormFieldURL), a.reportPath()).setReq().setToolTip(I18n.get(SBFReportStr.hintFormFieldURL)))
                    )
                    .add(filterSet = new FilterSet(I18n.get(SBFReportStr.titleReportFilters), LABEL_WIDTH))
                    .add(new FSet<CustomReportModel>(I18n.get(SBFReportStr.titleFormParams))
                            .add(new CheckHandler<>(I18n.get(SBFReportStr.titleFormFieldIncId), a.includeIdRow()).setToolTip(I18n.get(SBFReportStr.hintFormFieldIncId)))
                    )
            );
            b.add(new Tab<CustomReportModel>(I18n.get(SBFReportStr.tabFormHeaderSQL), 0)
                    .add(new FSet<CustomReportModel>(I18n.get(SBFReportStr.titleFormHeaderSQL))
                            .add(new TextAreaHandler<>(null, a.headerSQL()).setH(440).setToolTip(I18n.get(SBFReportStr.hintFormHeaderSQL)))
                    )
            );
        }

        @Override
        protected void createEditors(final TabPanel tabEditors) {
            super.createEditors(tabEditors);
            vlc = (VerticalLayoutContainer) tabEditors.getWidget(0);
            vlc.add(createParamContainer(), VLC.FILL);
            paramEditStore.addStoreHandlers(new ParamChangeHandler(this));
        }

        @Override
        protected void formToData(CustomReportModel dataModel) {
            super.formToData(dataModel);

            paramEditStore.commitChanges();
            List<CustomReportParamModel> edl = paramEditStore.getAll();
            edl.forEach(p -> checkParam(p));
            List<CustomReportParamModel> pars = new ArrayList<>(edl);
            pars.forEach(p -> {
                if (p.isNew()) {
                    p.setId(null);
                }
            });
            dataModel.setParams(pars);

            dataModel.setFilters(filterSet.getValues());
        }

        @Override
        protected void dataToForm(CustomReportModel dataModel) {
            super.dataToForm(dataModel);

            paramEditStore.rejectChanges();
            paramEditStore.clear();
            if (dataModel.getParams() != null) {
                paramEditStore.addAll(dataModel.getParams());
                dataModel.getParams().forEach(pm -> {
                    if (pm.getId().compareTo(maxId) > 0) {
                        maxId = pm.getId();
                    }
                });
            }

            filterSet.setFilterInfos(dataModel.getFilterInfos());
            filterSet.setVisible(dataModel.getFilterInfos() != null && !dataModel.getFilterInfos().isEmpty());
            filterSet.setValues(dataModel.getFilters());

            update();
        }

        private void checkParam(CustomReportParamModel pm) {
            if (null == pm.getCode() || pm.getCode().trim().length() == 0) {
                throw new ApplicationException(SBFReportStr.errorParamCodeEmpty);
            }
            if (null == pm.getParamType()) {
                throw new ApplicationException(SBFReportStr.errorParamTypeEmpty);
            }
            if (null == pm.getName() || pm.getName().trim().length() == 0) {
                throw new ApplicationException(SBFReportStr.errorParamNameEmpty);
            }
        }

        private void update() {
            if (cmd == null) {
                Scheduler.get().scheduleDeferred(cmd = new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        cmd = null;
                        actionManager.updateState();

                    }
                });
            }
        }

        private void doLayout() {
            Scheduler.get().scheduleFinally(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    vlc.forceLayout();
                }
            });
        }

        private ContentPanel createParamContainer() {
            ParamColumns cols = new ParamColumns();
            ColumnModel<CustomReportParamModel> cm = new ColumnModel<>(cols.getList());
            ParamEditAccess a = GWT.create(ParamEditAccess.class);
            paramEditStore = new ListStore<>(a.key());
            paramEditStore.setAutoCommit(true);

            final Grid<CustomReportParamModel> grid = new Grid<>(paramEditStore, cm);
            grid.setBorders(true);
            grid.getView().setAutoExpandColumn(cols.getNameColumn());
            grid.getView().setForceFit(true);
            grid.getView().setTrackMouseOver(false);
            grid.addCellClickHandler((CellClickEvent event) -> update());
            grid.getSelectionModel().addSelectionChangedHandler(ev -> update());
            grid.addRefreshHandler((RefreshEvent event) -> update());
            paramEditStore.addSortInfo(new Store.StoreSortInfo<>(a.code(), SortDir.ASC));
            paramEditStore.addStoreAddHandler((StoreAddEvent<CustomReportParamModel> event) -> update());

            cols.makeEditable(grid);
            actionManager = new ActionManager();

            CustomToolBar toolBar = new CustomToolBar(actionManager, true);
            toolBar.addButton(new AbstractAction(SBFBrowserStr.menuOperRowInsert, SBFResources.BROWSER_ICONS.RowInsert16(), SBFResources.BROWSER_ICONS.RowInsert()) {
                @Override
                protected void onExecute() {
                    CustomReportParamModel m = createParamModel();
                    paramEditStore.add(m);
                }
            });
            toolBar.addButton(new AbstractAction(SBFBrowserStr.menuOperRowDelete, SBFResources.BROWSER_ICONS.RowDelete16(), SBFResources.BROWSER_ICONS.RowDelete()) {
                @Override
                protected void onExecute() {
                    CustomReportParamModel m = grid.getSelectionModel().getSelectedItem();
                    if (m != null) {
                        paramEditStore.remove(m);
                    }
                }

                @Override
                public boolean checkEnabled() {
                    return super.checkEnabled() && grid.getSelectionModel().getSelectedItem() != null;
                }

            });

            VerticalLayoutContainer vc = new VerticalLayoutContainer();
            vc.add(toolBar, VLC.CONST);
            vc.add(grid, VLC.FILL);

            final ContentPanel cp = new ContentPanel();
            cp.setHeaderVisible(true);
            cp.setHeading(I18n.get(SBFReportStr.titleAddParameters));
            cp.setWidget(vc);
            cp.setPixelSize(920, 410);
            cp.addStyleName("margin-10");

            return cp;

        }

        private CustomReportParamModel createParamModel() {
            CustomReportParamModel model = new CustomReportParamModel();
            maxId = maxId.add(BigDecimal.ONE);
            model.setNew(true);
            model.setId(maxId);
            return model;
        }

        private class ParamChangeHandler extends StoreDataMutationHandler<CustomReportParamModel> {

            private final BaseForm f;

            public ParamChangeHandler(BaseForm f) {
                this.f = f;
            }

            @Override
            public void onDataMutation(StoreDataMutationEvent<CustomReportParamModel> event) {
                f.setChanged(true);
                ((CustomReportFormImpl) f).update();
            }
        }

        private class FilterSet extends FSet {

            private final int labelWidth;
            private List<CustomReportFilterInfo> filterInfos;
            private final Map<String, TextField> fieldMap = new HashMap<>();

            public FilterSet(String cap, int labelWidth) {
                super(cap);
                this.labelWidth = labelWidth;
            }

            public void setFilterInfos(List<CustomReportFilterInfo> filterInfos) {
                if (equalsFilterInfos(filterInfos, this.filterInfos)) {
                    return;
                }
                this.filterInfos = filterInfos;
                final VerticalFieldSet fs = getManagedSet();
                clearFields(fs);
                if (filterInfos != null) {
                    for (CustomReportFilterInfo filterInfo : filterInfos) {
                        TextField field = new TrimTextField();
                        fieldMap.put(filterInfo.getCode(), field);
                        FieldLabel flabel = new FieldLabel(field, I18n.get(filterInfo.getDescription()) + " (" + filterInfo.getCode() + ")");
                        fs.add(flabel);
                    }
                }
                updateFieldsInfo(fs);
                SbfFieldHelper.updateLabelsWidth(fs, labelWidth);
                SbfFieldHelper.updateLabelsAlign(fs);
            }

            public List<StringParamInfo> getValues() {
                List<StringParamInfo> values = new ArrayList<>();
                fieldMap.forEach((code, field) -> {
                    String val = field.getValue();
                    if (val != null && !val.isEmpty()) {
                        values.add(new StringParamInfo(code, val));
                    }
                });
                return values;
            }

            public void setValues(List<StringParamInfo> values) {
                fieldMap.forEach((code, field) -> {
                    field.setValue(null);
                });
                if (values != null) {
                    values.forEach(val -> {
                        TextField field = fieldMap.get(val.getName());
                        if (field != null) {
                            field.setValue(val.getValue());
                        }
                    });
                }
            }

            private void clearFields(VerticalFieldSet fs) {
                Map<Widget, BaseAdapter> formFields = getFields();
                for (int i = 0; i < fs.getWidgetCount(); i++) {
                    formFields.remove(fs.getWidget(i));
                }
                fs.clear();
                fieldMap.clear();
            }

            private boolean equalsFilterInfos(List<CustomReportFilterInfo> fi1, List<CustomReportFilterInfo> fi2) {
                if (fi1 == null && fi2 == null) {
                    return true;
                }
                if (fi1 == null || fi2 == null || fi1.size() != fi2.size()) {
                    return false;
                }
                for (int i = 0; i < fi1.size(); i++) {
                    CustomReportFilterInfo f1 = fi1.get(i);
                    CustomReportFilterInfo f2 = fi2.get(i);
                    if (!f1.equals(f2)) {
                        return false;
                    }
                }
                return true;
            }
        }

        private class ParamColumns {

            private final ColumnConfig<CustomReportParamModel, String> codeColumn;
            private final ColumnConfig<CustomReportParamModel, CustomReportParamType> typeColumn;
            private final ColumnConfig<CustomReportParamModel, String> nameColumn;

            public ParamColumns() {
                ParamEditAccess a = GWT.create(ParamEditAccess.class);

                codeColumn = new ColumnConfig<>(a.code(), 100, "код");
                codeColumn.setFixed(true);
                typeColumn = new ColumnConfig<>(a.paramType(), 100, "тип");
                typeColumn.setCell(new SimpleSafeHtmlCell<CustomReportParamType>(new AbstractSafeHtmlRenderer<CustomReportParamType>() {
                    @Override
                    public SafeHtml render(CustomReportParamType type) {
                        return SafeHtmlUtils.fromString(type != null ? I18n.get(type.getItemName()) : "");
                    }
                }));
                typeColumn.setFixed(true);
                nameColumn = new ColumnConfig<>(a.name(), 400, "наименование");
            }

            public List<ColumnConfig<CustomReportParamModel, ?>> getList() {
                List<ColumnConfig<CustomReportParamModel, ?>> l = new ArrayList<>();
                l.add(codeColumn);
                l.add(typeColumn);
                l.add(nameColumn);
                return l;
            }

            public void makeEditable(Grid<CustomReportParamModel> g) {
                final GridInlineEditing<CustomReportParamModel> ed = new GridInlineEditing<>(g);

                g.setSelectionModel(new CellSelectionModel<>());

                ComboBox<CustomReportParamType> typeCombo = new ComboBox<CustomReportParamType>(createTypesStore(), new LabelProvider<CustomReportParamType>() {
                    @Override
                    public String getLabel(CustomReportParamType item) {
                        return I18n.get(item.getItemName());
                    }
                });
                typeCombo.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
                typeCombo.setForceSelection(true);
                typeCombo.setAllowBlank(false);
                typeCombo.setEditable(false);
                TextField codeEdit = new TextField();
                codeEdit.setAllowBlank(false);
                TextField nameEdit = new TextField();
                nameEdit.setAllowBlank(false);
                ed.addEditor(codeColumn, codeEdit);
                ed.addEditor(typeColumn, typeCombo);
                ed.addEditor(nameColumn, nameEdit);
            }

            private ListStore<CustomReportParamType> createTypesStore() {
                ListStore<CustomReportParamType> types = new ListStore<CustomReportParamType>(new ModelKeyProvider<CustomReportParamType>() {
                    @Override
                    public String getKey(CustomReportParamType item) {
                        return item.getCode();
                    }
                });
                types.addAll(Arrays.asList(CustomReportParamType.values()));
                return types;
            }

            public ColumnConfig<CustomReportParamModel, String> getCodeColumn() {
                return codeColumn;
            }

            public ColumnConfig<CustomReportParamModel, CustomReportParamType> getTypeColumn() {
                return typeColumn;
            }

            public ColumnConfig<CustomReportParamModel, String> getNameColumn() {
                return nameColumn;
            }

        }
    }

    public interface ParamEditAccess extends PropertyAccess<CustomReportParamModel> {

        @Editor.Path("id")
        ModelKeyProvider<CustomReportParamModel> key();

        ValueProvider<CustomReportParamModel, BigDecimal> id();

        ValueProvider<CustomReportParamModel, String> code();

        ValueProvider<CustomReportParamModel, CustomReportParamType> paramType();

        ValueProvider<CustomReportParamModel, String> name();

    }

}
