package ru.sbsoft.client.components.grid.editable;

import ru.sbsoft.client.components.grid.KeyNameRenderer;
import ru.sbsoft.client.components.NoRestrictStringComboBoxCell;
import ru.sbsoft.client.components.form.LookupComboBox;
import ru.sbsoft.client.components.form.LongStrComboBox;
import ru.sbsoft.client.components.form.BooleanStrComboBox;
import ru.sbsoft.client.components.form.model.LongStrComboBoxModel;
import ru.sbsoft.client.components.form.model.BooleanStrComboBoxModel;
import com.google.gwt.i18n.client.NumberFormat;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import java.math.BigDecimal;
import java.math.RoundingMode;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.SBFEntryPoint;
import ru.sbsoft.client.components.browser.filter.DictionaryLoader;
import ru.sbsoft.client.components.field.DateField;
import ru.sbsoft.client.components.field.DateTimeField;
import ru.sbsoft.client.components.form.BoundedTextField;
import ru.sbsoft.client.components.form.LookupItem;
import ru.sbsoft.client.components.form.LookupLabelProvider;
import ru.sbsoft.client.components.form.StrStrComboBox;
import ru.sbsoft.client.components.form.model.StrStrComboBoxModel;
import ru.sbsoft.client.components.grid.StringComboBoxRenderer;
import ru.sbsoft.client.filter.editor.RangeFilterAdapter;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.shared.meta.Column;
import ru.sbsoft.shared.meta.ColumnType;
import ru.sbsoft.shared.meta.LookupKeyType;
import ru.sbsoft.shared.meta.UpdateInfo;
import ru.sbsoft.shared.renderer.Renderer;

public class EditableFieldFactory {

    private final Column column;
    private final Renderer<?> renderer;
    private final int length;
    private final boolean rounded;
    private final DictionaryLoader dictionaryLoader;
    private LookupComboBox lookupComboBox;

    public EditableFieldFactory(Column metaColumn, DictionaryLoader dictionaryLoader) {
        this.column = metaColumn;
        this.dictionaryLoader = dictionaryLoader;
        this.renderer = createRenderer(metaColumn);
        final UpdateInfo updateInfo = metaColumn.getUpdateInfo();
        this.length = (null == updateInfo) ? 0 : updateInfo.getLenght();
        this.rounded = metaColumn.getExpCellFormat() == null; // если есть форматирование на ячейку - отключаем округление на колонку
    }

    private Renderer createRenderer(final Column metaColumn) {
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

    public Field createField() {
        if (null != column.getLookupGridInfo()) {
            return getLookup();
        }
        if (null != renderer) {
            return createRenderComboBox();
        }
        return createSimpleField();
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
                final NumberFormat format;
                if (null == column.getFormat()) {
                    format = (this.length == 0) ? NumberFormat.getFormat("0.00")
                            : NumberFormat.getFormat("0.".concat(ClientUtils.repeat("0", this.length)));

                } else {
                    format = NumberFormat.getFormat(column.getFormat());
                }
                NumberField f = new NumberField<BigDecimal>(
                        new NumberPropertyEditor.BigDecimalPropertyEditor(format));
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
                return new NumberField<Long>(new NumberPropertyEditor.LongPropertyEditor());
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

        if (column.getType() == ColumnType.CURRENCY) {
            return createCurrencyConverter();
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

    private Converter createCurrencyConverter() {
        return new Converter<BigDecimal, BigDecimal>() {

            @Override
            public BigDecimal convertFieldValue(BigDecimal fieldValue) {
                if (rounded) {
                    return fieldValue == null ? null : fieldValue.setScale(length, RoundingMode.HALF_UP);
                }
                return fieldValue;
            }

            @Override
            public BigDecimal convertModelValue(BigDecimal modelValue) {
                if (rounded) {
                    return modelValue == null ? null : modelValue.setScale(length, RoundingMode.HALF_UP);
                }
                return modelValue;
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
}
