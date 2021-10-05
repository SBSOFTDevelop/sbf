package ru.sbsoft.shared.meta;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.sbsoft.shared.Condition;
import ru.sbsoft.shared.grid.style.ConditionalCellStyle;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;
import ru.sbsoft.shared.grid.condition.IGridCondition;
import ru.sbsoft.shared.grid.format.IExpCellFormat;
import ru.sbsoft.shared.meta.config.ColumnCfg;
import ru.sbsoft.shared.meta.filter.Dictionary;
import ru.sbsoft.shared.model.ImageBase64;

/**
 * @author balandin
 */
public class Column implements Serializable {

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
    //
    private String alias;
    private transient int index;
    //
    public static final String RENDERER_PREFIX = "sum";
    public static final String COLUMN_CONFIG_PREFIX = "cfg";
    public static final String TITLE_PREFIX = "t";
    //
    private Map<?, ILocalizedString> valueSet = null;
    //
    private transient Map<String, Object> data;
    //
    private UpdateInfo updateInfo;
    //
    private IGridCondition editCondition;
    //
    private IColumnCustomInfo customInfo;
    //
    private IExpCellFormat expCellFormat;

    private ColumnWrapType wordWrap;

    public ColumnWrapType getWordWrap() {
        return wordWrap;
    }

    public void setWordWrap(ColumnWrapType wordWrap) {
        this.wordWrap = wordWrap;
    }

    public IExpCellFormat getExpCellFormat() {
        return expCellFormat;
    }

    public void setExpCellFormat(IExpCellFormat expCellFormat) {
        this.expCellFormat = expCellFormat;
    }

    public Column() {
    }

    public ILocalizedString getCaption() {
        return caption != null ? caption : new NonLocalizedString(alias);
    }

    public void setCaption(ILocalizedString caption) {
        this.caption = caption;
    }

    public ILocalizedString getDescription() {
        return description;
    }

    public void setDescription(ILocalizedString description) {
        this.description = description;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public ColumnType getType() {
        return type;
    }

    public void setType(ColumnType type) {
        this.type = type;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ColumnGroup getGroup() {
        return group;
    }

    public void setGroup(ColumnGroup group) {
        this.group = group;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isFiltered() {
        return filtered;
    }

    public void setFiltered(boolean filtered) {
        this.filtered = filtered;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public Object getData(String key) {
        if (data != null) {
            return data.get(key);
        }
        return null;
    }

    public Object setData(String key, Object value) {
        if (data == null) {
            data = new HashMap<String, Object>();
        }
        return data.put(key, value);
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<Style> getStyles() {
        return styles;
    }

    public void setStyles(List<Style> styles) {
        this.styles = styles;
    }

    public List<ConditionalCellStyle> getGridStyles() {
        return gridStyles;
    }

    public void setGridStyles(List<ConditionalCellStyle> gridStyles) {
        this.gridStyles = gridStyles;
    }

    public List<Condition> getFilterConditions() {
        return filterConditions;
    }

    public void setFilterConditions(List<Condition> filterConditions) {
        this.filterConditions = filterConditions;
    }

    public boolean isEnumerated() {
        return enumerated;
    }

    public void setEnumerated(boolean enumerated) {
        this.enumerated = enumerated;
    }

    public LookupGridInfo getLookupGridInfo() {
        return lookupGridInfo;
    }

    public void setLookupGridInfo(LookupGridInfo lookupGridInfo) {
        this.lookupGridInfo = lookupGridInfo;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public Map<?, ImageBase64> getIconMap() {
        return iconMap;
    }

    public void setIconMap(Map<Object, ImageBase64> iconMap) {
        this.iconMap = iconMap;
    }

    public boolean isComparable(Column column) {
        return column != null && column != this && column.getType().getProtoType() == type.getProtoType();
    }

    public Map<?, ILocalizedString> getValueSet() {
        return valueSet;
    }

    public void setValueSet(Map<?, ILocalizedString> valueSet) {
        this.valueSet = valueSet;
    }

    public ColumnCfg getConfig() {
        ColumnCfg column = new ColumnCfg();
        column.setWidth(getWidth());
        column.setVisible(isVisible() && !isHidden());
        column.setAlias(getAlias());
        return column;
    }

    public void setConfig(ColumnCfg config) {
        setWidth(config.getWidth());
        setVisible(config.isVisible());
    }

    public UpdateInfo getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(UpdateInfo updateInfo) {
        this.updateInfo = updateInfo;
    }

    public IGridCondition getEditCondition() {
        return editCondition;
    }

    public void setEditCondition(IGridCondition editCondition) {
        this.editCondition = editCondition;
    }

    public List<String> getCombo() {
        return combo;
    }

    public void setCombo(List<String> combo) {
        this.combo = combo;
    }

    public IColumnCustomInfo getCustomInfo() {
        return customInfo;
    }

    public void setCustomInfo(IColumnCustomInfo customInfo) {
        this.customInfo = customInfo;
    }
}
