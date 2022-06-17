package ru.sbsoft.client.components.grid.editable;

import ru.sbsoft.client.components.grid.KeyNameRenderer;
import ru.sbsoft.client.components.NoRestrictStringComboBoxCell;
import ru.sbsoft.client.components.form.LookupComboBox;
import ru.sbsoft.client.components.form.LongStrComboBox;
import ru.sbsoft.client.components.form.BooleanStrComboBox;
import ru.sbsoft.client.components.form.model.LongStrComboBoxModel;
import ru.sbsoft.client.components.form.model.BooleanStrComboBoxModel;
import com.google.gwt.i18n.client.NumberFormat;
import ru.sbsoft.svc.cell.core.client.form.ComboBoxCell;
import ru.sbsoft.svc.data.shared.Converter;
import ru.sbsoft.svc.data.shared.ListStore;
import ru.sbsoft.svc.widget.core.client.form.CheckBox;
import ru.sbsoft.svc.widget.core.client.form.Field;
import ru.sbsoft.svc.widget.core.client.form.IsField;
import ru.sbsoft.svc.widget.core.client.form.NumberField;
import ru.sbsoft.svc.widget.core.client.form.NumberPropertyEditor;
import java.math.BigDecimal;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.SBFEntryPoint;
import ru.sbsoft.client.components.IValSelectHandler;
import ru.sbsoft.client.components.ValSelectField;
import ru.sbsoft.client.components.browser.SelectBrowserFactory;
import ru.sbsoft.client.components.browser.filter.DictionaryLoader;
import ru.sbsoft.client.components.field.DateField;
import ru.sbsoft.client.components.field.DateTimeField;
import ru.sbsoft.client.components.form.BoundedTextField;
import ru.sbsoft.client.components.form.LookupItem;
import ru.sbsoft.client.components.form.LookupLabelProvider;
import ru.sbsoft.client.components.form.StrStrComboBox;
import ru.sbsoft.client.components.form.handler.LookupFieldEx;
import ru.sbsoft.client.components.form.model.StrStrComboBoxModel;
import ru.sbsoft.client.components.grid.StringComboBoxRenderer;
import ru.sbsoft.client.components.tree.TreeSelector;
import ru.sbsoft.client.components.tree.TreeShowMode;
import ru.sbsoft.client.filter.editor.RangeFilterAdapter;
import ru.sbsoft.shared.ICodeNameTreeNode;
import ru.sbsoft.shared.TreeNode;
import ru.sbsoft.shared.TreeNodeModel;
import ru.sbsoft.shared.interfaces.NamedGridType;
import ru.sbsoft.shared.interfaces.TreeType;
import ru.sbsoft.shared.meta.ColumnType;
import ru.sbsoft.shared.meta.IColumn;
import ru.sbsoft.shared.meta.IValueSelectorConfig;
import ru.sbsoft.shared.meta.LookupKeyType;
import ru.sbsoft.shared.model.LookupCellType;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.shared.renderer.Renderer;
import ru.sbsoft.shared.util.IdNameLong;

public class EditableFieldFactory {

    private final IColumn column;
    private final Renderer<?> renderer;
    private final int length;
    private final DictionaryLoader dictionaryLoader;
    private LookupComboBox lookupComboBox;

    public EditableFieldFactory(IColumn metaColumn, DictionaryLoader dictionaryLoader) {
        this.column = metaColumn;
        this.dictionaryLoader = dictionaryLoader;
        this.renderer = createRenderer(metaColumn);
        Integer maxLen = metaColumn.getMaxLength();
        this.length = maxLen != null ? maxLen : -1;
    }

    private Renderer createRenderer(final IColumn metaColumn) {
        final Renderer result;
        if (null != metaColumn.getValueSet()) {
            return new KeyNameRenderer(metaColumn.getValueSet());
        } else if (null != metaColumn.getFormat()) {
            result = SBFEntryPoint.getRendererManager().getRenderer(metaColumn.getFormat());
        } else if (null != metaColumn.getCombo()) {
            result = new StringComboBoxRenderer(metaColumn.getCombo());
        } else {
            result = null;
        }
        return result;
    }

    protected LookupComboBox getLookup() {
        if (null == lookupComboBox) {
            lookupComboBox = createLookup();
        }
        return lookupComboBox;
    }

    public IsField createField() {
        if (ColumnType.ID_NAME == column.getType()) {
            return createIdNameSelectorField();
        }
        if (null != column.getLookupGridInfo()) {
            return getLookup();
        }
        if (null != renderer) {
            return createRenderComboBox();
        }
        return createSimpleField();
    }

    private IsField createIdNameSelectorField() {
        IValueSelectorConfig<?> cfg = column.getValueSelectorConfig();
        if (cfg.getBrowserType() instanceof NamedGridType) {
            NamedGridType gridType = (NamedGridType) cfg.getBrowserType();
            LookupFieldEx<LookupInfoModel> f = new LookupFieldEx<>();
            f.setFieldVisible(cfg.isShowCode() ? LookupCellType.NAME : LookupCellType.KEY, false);
            f.setSeparatorsVisible(false);
            f.setSelectBrowserMaker(new SelectBrowserFactory(gridType));
            f.setFilterSource(() -> cfg.getFilters());
            return f;
        } else if (cfg.getBrowserType() instanceof TreeType) {
            TreeType treeType = (TreeType) cfg.getBrowserType();
            TreeSelector<BigDecimal> selector = new TreeSelector<BigDecimal>(treeType).setSelectLeafOnly(false).setTitle("Выбор элемента");
            selector.setFilter(cfg.getFilters(), null);
            selector.setShowMode(TreeShowMode.SHORT);
            ValSelectField<TreeNode<BigDecimal>> f = new IdNameTreeSelector(selector, true);
            return f;
        }
        return null;
    }

    private LookupComboBox createLookup() {
        final LookupComboBox comboBox = new LookupComboBox(
                new NoRestrictStringComboBoxCell(new LookupLabelProvider()));
        comboBox.setWidth(RangeFilterAdapter.SINGLE_WIDTH);
        comboBox.setMinListWidth(300);
        comboBox.setAutoValidate(true);
        comboBox.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        dictionaryLoader.load(column, comboBox, column.getLookupGridInfo());

        return comboBox;
    }

    private Field createSimpleField() {
        switch (column.getType()) {
            case BOOL:
                return new CheckBox();
            case CURRENCY:
                final NumberField f;
                if (column.getExpCellFormat() != null) {
                    f = new CellBigDecimalNumberField(new CellBigDecimalPropertyEditor(column.getExpCellFormat()));
                } else {
                    final NumberFormat format;
                    if (column.getFormat() != null) {
                        format = NumberFormat.getFormat(column.getFormat());
                    } else {
                        format = NumberFormat.getFormat("0.00");
                    }
                    f = new NumberField<BigDecimal>(new NumberPropertyEditor.BigDecimalPropertyEditor(format));
                }
                f.getCell().getInputElement(f.getElement()).setPropertyString("autocomplete", "off");
                return f;
            case DATE:
                return new DateField();
            case DATE_TIME:
            case TIMESTAMP:
                return new DateTimeField();
            case INTEGER:
            case KEY:
            case IDENTIFIER:
            case TEMPORAL_KEY:
                return new NumberField<>(new NumberPropertyEditor.LongPropertyEditor());
            case VCHAR:
                return new BoundedTextField(length);
            default:
                return null;
        }
    }

    private Field createRenderComboBox() {
        switch (column.getType()) {
            case BOOL:
                return createRenderBooleanComboBox();
            case INTEGER:
                return createRenderLongComboBox();
            case VCHAR:
                return createRenderStrComboBox();
            default:
                return null;
        }
    }

    private BooleanStrComboBox createRenderBooleanComboBox() {
        final BooleanStrComboBox comboBox = new BooleanStrComboBox();
        for (Renderer.Entry item : renderer.getItems()) {
            if (item.getKey() instanceof Boolean) {
                comboBox.add(new BooleanStrComboBoxModel((Boolean) item.getKey(), I18n.get(item.getValue())));
            }
        }
        return comboBox;
    }

    private LongStrComboBox createRenderLongComboBox() {
        final LongStrComboBox comboBox = new LongStrComboBox();
        for (Renderer.Entry item : renderer.getItems()) {
            if (item.getKey() instanceof Long) {
                comboBox.add(new LongStrComboBoxModel((Long) item.getKey(), I18n.get(item.getValue())));
            }
        }
        return comboBox;
    }

    private StrStrComboBox createRenderStrComboBox() {
        final StrStrComboBox comboBox = new StrStrComboBox();
        comboBox.setEditable(true);
        for (Renderer.Entry item : renderer.getItems()) {
            comboBox.add(new StrStrComboBoxModel(item.getKey().toString(), I18n.get(item.getValue())));
        }
        return comboBox;
    }

    public Converter createConverter() {
        if (ColumnType.ID_NAME == column.getType()) {
            IValueSelectorConfig<?> cfg = column.getValueSelectorConfig();
            if (cfg.getBrowserType() instanceof NamedGridType) {
                return createIdNameGridConverter(cfg);
            } else if (cfg.getBrowserType() instanceof TreeType) {
                return createIdNameTreeConverter(cfg);
            }
        }
        if (null != column.getLookupGridInfo()) {
            return createLookupConverter();
        }
        if (null == renderer) {
            return null;
        }
        switch (column.getType()) {
            case BOOL:
                return createBooleanConverter();
            case INTEGER:
                return createLongConverter();
            case VCHAR:
                return createStrConverter();
            default:
                return null;
        }
    }

    private Converter createIdNameGridConverter(IValueSelectorConfig<?> cfg) {
        return new Converter<IdNameLong, LookupInfoModel>() {
            @Override
            public IdNameLong convertFieldValue(LookupInfoModel lookup) {
                return lookup != null && lookup.getID() != null ? new IdNameLong(lookup.getID().longValueExact(), cfg.isShowCode() ? lookup.getSemanticKey() : lookup.getSemanticName()) : null;
            }

            @Override
            public LookupInfoModel convertModelValue(IdNameLong val) {
                return val != null && val.getId() != null ? new LookupInfoModel(BigDecimal.valueOf(val.getId()), val.getName(), val.getName()) : null;
            }
        };
    }

    private Converter createIdNameTreeConverter(IValueSelectorConfig<?> cfg) {
        return new Converter<IdNameLong, TreeNode<BigDecimal>>() {
            @Override
            public IdNameLong convertFieldValue(TreeNode<BigDecimal> node) {
                if (node == null || node.getKey() == null) {
                    return null;
                } else {
                    final String name;
                    if (node instanceof ICodeNameTreeNode) {
                        ICodeNameTreeNode cntn = (ICodeNameTreeNode) node;
                        name = cfg.isShowCode() ? cntn.getCode() : cntn.getName();
                    } else {
                        name = node.getTitle();
                    }
                    return new IdNameLong(node.getKey().longValueExact(), name);
                }
            }

            @Override
            public TreeNode<BigDecimal> convertModelValue(IdNameLong val) {
                return val != null && val.getId() != null ? new TreeNodeModel<>(BigDecimal.valueOf(val.getId()), val.getName(), false) : null;
            }
        };
    }

    private Converter createLookupConverter() {
        return new Converter<String, LookupItem>() {

            @Override
            public String convertFieldValue(LookupItem fieldValue) {
                if (LookupKeyType.KEY_CODE == column.getLookupGridInfo().getKeyType()) {
                    return fieldValue.getKey().toString();
                } else {
                    return fieldValue.getValue();
                }
            }

            @Override
            public LookupItem convertModelValue(String modelValue) {
                final ListStore<LookupItem> store = getLookup().getStore();
                for (final LookupItem item : store.getAll()) {
                    if (LookupKeyType.KEY_CODE == column.getLookupGridInfo().getKeyType()) {
                        if (item.getKey().toString().equals(modelValue)) {
                            return item;
                        }
                    } else {
                        if (item.getValue().equals(modelValue)) {
                            return item;
                        }
                    }
                }
                return null;
            }
        };
    }

    private Converter createBooleanConverter() {
        return new Converter<Boolean, BooleanStrComboBoxModel>() {

            @Override
            public Boolean convertFieldValue(BooleanStrComboBoxModel fieldValue) {
                return fieldValue.getId();
            }

            @Override
            public BooleanStrComboBoxModel convertModelValue(Boolean modelValue) {
                final Renderer<Boolean> locRenderer = (Renderer<Boolean>) renderer;
                return new BooleanStrComboBoxModel(modelValue, I18n.get(locRenderer.render(modelValue)));
            }
        };
    }

    private Converter createLongConverter() {
        return new Converter<Long, LongStrComboBoxModel>() {

            @Override
            public Long convertFieldValue(LongStrComboBoxModel fieldValue) {
                return fieldValue.getId();
            }

            @Override
            public LongStrComboBoxModel convertModelValue(Long modelValue) {
                final Renderer<Long> locRenderer = (Renderer<Long>) renderer;
                return new LongStrComboBoxModel(modelValue, I18n.get(locRenderer.render(modelValue)));
            }
        };
    }

    private Converter createStrConverter() {
        return new Converter<String, StrStrComboBoxModel>() {

            @Override
            public String convertFieldValue(StrStrComboBoxModel fieldValue) {
                if (null == fieldValue) {
                    return null;
                }
                return fieldValue.getId();
            }

            @Override
            public StrStrComboBoxModel convertModelValue(String modelValue) {
                final Renderer<String> locRenderer = (Renderer<String>) renderer;
                return new StrStrComboBoxModel(modelValue, I18n.get(locRenderer.render(modelValue)));
            }
        };
    }
    
    private class IdNameTreeSelector extends ValSelectField<TreeNode<BigDecimal>> {
        
        public IdNameTreeSelector(IValSelectHandler<TreeNode<BigDecimal>> h, boolean isCleansable) {
            super(h, isCleansable);
        }

        @Override
        public void setValue(TreeNode<BigDecimal> value) {
            super.setValue(value, false);
        }
        
    }
}
