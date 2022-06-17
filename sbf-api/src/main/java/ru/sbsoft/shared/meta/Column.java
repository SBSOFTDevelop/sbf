package ru.sbsoft.shared.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.sbsoft.shared.Condition;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.grid.style.ConditionalCellStyle;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;
import ru.sbsoft.shared.grid.condition.IGridCondition;
import ru.sbsoft.shared.grid.format.IExpCellFormat;
import ru.sbsoft.shared.interfaces.GridType;
import ru.sbsoft.shared.interfaces.ObjectType;
import ru.sbsoft.shared.interfaces.TreeType;
import ru.sbsoft.shared.meta.config.ColumnCfg;
import ru.sbsoft.shared.meta.filter.Dictionary;
import ru.sbsoft.shared.model.ImageBase64;
import sun.misc.ObjectInputFilter;

/**
 * @author balandin
 */
public class Column implements Serializable, IColumn {

    private ColumnType type;
    private ILocalizedString caption;
    private ILocalizedString description;
    private int width;
    private boolean visible;
    private boolean hidden;
    private boolean filtered;
    private boolean frozen;
    private String format;
    private ColumnGroup group;
    private List<Style> styles;
    private boolean enumerated;
    protected LookupGridInfo lookupGridInfo;
    protected Dictionary dictionary;
    protected List<String> combo;
    private Map<?, ImageBase64> iconMap = null;
    private List<ConditionalCellStyle> gridStyles;
    private List<Condition> filterConditions;
    private IValueSelectorConfig<ObjectType> valueSelectorConfig;
    //
    private String alias;
    private int index;
    //
    private Map<?, ILocalizedString> valueSet = null;
    //
    private transient Map<String, Object> data;
    //
//    private UpdateInfo updateInfo;
    private boolean forUpdate = false;
    private Integer maxLength;
    //
    private IGridCondition editCondition;
    private IGridCondition signAllCondition;
    private IGridCondition signPositiveCondition;

    //
    private IColumnCustomInfo customInfo;
    //
    private IExpCellFormat expCellFormat;

    private ColumnWrapType wordWrap;

    public Column() {
    }

    @Override
    public ColumnWrapType getWordWrap() {
        return wordWrap;
    }

    public void setWordWrap(ColumnWrapType wordWrap) {
        this.wordWrap = wordWrap;
    }

    @Override
    public IExpCellFormat getExpCellFormat() {
        return expCellFormat;
    }

    public void setExpCellFormat(IExpCellFormat expCellFormat) {
        this.expCellFormat = expCellFormat;
    }

    @Override
    public ILocalizedString getCaption() {
        return caption != null ? caption : new NonLocalizedString(alias);
    }

    public void setCaption(ILocalizedString caption) {
        this.caption = caption;
    }

    @Override
    public ILocalizedString getDescription() {
        return description;
    }

    public void setDescription(ILocalizedString description) {
        this.description = description;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public ColumnType getType() {
        return type;
    }

    public void setType(ColumnType type) {
        this.type = type;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public ColumnGroup getGroup() {
        return group;
    }

    public void setGroup(ColumnGroup group) {
        this.group = group;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean isFiltered() {
        return filtered;
    }

    public void setFiltered(boolean filtered) {
        this.filtered = filtered;
    }

    @Override
    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    @Override
    public Object getData(String key) {
        if (data != null) {
            return data.get(key);
        }
        return null;
    }

    @Override
    public Object setData(String key, Object value) {
        if (data == null) {
            data = new HashMap<>();
        }
        return data.put(key, value);
    }

    @Override
    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public List<Style> getStyles() {
        return styles;
    }

    public void setStyles(List<Style> styles) {
        this.styles = styles;
    }

    @Override
    public List<ConditionalCellStyle> getGridStyles() {
        return gridStyles;
    }

    public void setGridStyles(List<ConditionalCellStyle> gridStyles) {
        this.gridStyles = gridStyles;
    }

    @Override
    public List<Condition> getFilterConditions() {
        return filterConditions;
    }

    public void setFilterConditions(List<Condition> filterConditions) {
        this.filterConditions = filterConditions;
    }

    @Override
    public IValueSelectorConfig<ObjectType> getValueSelectorConfig() {
        return valueSelectorConfig;
    }

    public void setValueSelectorConfig(IValueSelectorConfig<ObjectType> valueSelectorConfig) {
        this.valueSelectorConfig = valueSelectorConfig;
    }

    @Override
    public boolean isEnumerated() {
        return enumerated;
    }

    public void setEnumerated(boolean enumerated) {
        this.enumerated = enumerated;
    }

    @Override
    public LookupGridInfo getLookupGridInfo() {
        return lookupGridInfo;
    }

    public void setLookupGridInfo(LookupGridInfo lookupGridInfo) {
        this.lookupGridInfo = lookupGridInfo;
    }

    @Override
    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public Map<?, ImageBase64> getIconMap() {
        return iconMap;
    }

    public void setIconMap(Map<Object, ImageBase64> iconMap) {
        this.iconMap = iconMap;
    }

    @Override
    public boolean isComparable(IColumn column) {
        return column != null && column != this && column.getType().getProtoType() == type.getProtoType();
    }

    @Override
    public Map<?, ILocalizedString> getValueSet() {
        return valueSet;
    }

    public void setValueSet(Map<?, ILocalizedString> valueSet) {
        this.valueSet = valueSet;
    }

    @Override
    public ColumnCfg getConfig() {
        ColumnCfg column = new ColumnCfg();
        column.setWidth(getWidth());
        column.setVisible(isVisible() && !isHidden());
        column.setAlias(getAlias());
        return column;
    }

    @Override
    public void applyConfig(ColumnCfg config) {
        setWidth(config.getWidth());
        setVisible(config.isVisible());
    }

    @Override
    public boolean isForUpdate() {
        return forUpdate;
    }

    public void setForUpdate(boolean forUpdate) {
        this.forUpdate = forUpdate;
    }

    @Override
    public IGridCondition getEditCondition() {
        return editCondition;
    }

    public void setEditCondition(IGridCondition editCondition) {
        this.editCondition = editCondition;
    }

    @Override
    public IGridCondition getSignAllCondition() {
        return signAllCondition;
    }

    public void setSignAllCondition(IGridCondition signAllCondition) {
        this.signAllCondition = signAllCondition;
    }

    @Override
    public IGridCondition getSignPositiveCondition() {
        return signPositiveCondition;
    }

    public void setSignPositiveCondition(IGridCondition signPositiveCondition) {
        this.signPositiveCondition = signPositiveCondition;
    }

    @Override
    public List<String> getCombo() {
        return combo;
    }

    public void setCombo(List<String> combo) {
        this.combo = combo;
    }

    @Override
    public IColumnCustomInfo getCustomInfo() {
        return customInfo;
    }

    public void setCustomInfo(IColumnCustomInfo customInfo) {
        this.customInfo = customInfo;
    }

    @Override
    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void replaceStyles(List<ConditionalCellStyle> gridStyles) {
        setGridStyles(gridStyles);
    }
}
