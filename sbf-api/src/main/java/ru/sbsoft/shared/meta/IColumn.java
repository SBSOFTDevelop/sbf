package ru.sbsoft.shared.meta;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import ru.sbsoft.shared.Condition;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.grid.condition.IGridCondition;
import ru.sbsoft.shared.grid.format.IExpCellFormat;
import ru.sbsoft.shared.grid.style.ConditionalCellStyle;
import ru.sbsoft.shared.interfaces.ObjectType;
import ru.sbsoft.shared.meta.config.ColumnCfg;
import ru.sbsoft.shared.meta.filter.Dictionary;
import ru.sbsoft.shared.model.ImageBase64;

/**
 *
 * @author vk
 */
public interface IColumn {

    public static final String RENDERER_PREFIX = "sum";
    public static final String COLUMN_CONFIG_PREFIX = "cfg";
    public static final String TITLE_PREFIX = "t";

    //
    String getAlias();

    ILocalizedString getCaption();

    List<String> getCombo();

    ColumnCfg getConfig();

    IColumnCustomInfo getCustomInfo();

    Object getData(String key);

    ILocalizedString getDescription();

    Dictionary getDictionary();

    IGridCondition getEditCondition();

    IExpCellFormat getExpCellFormat();

    List<Condition> getFilterConditions();

    String getFormat();

    List<ConditionalCellStyle> getGridStyles();
    
    IGridCondition getSignAllCondition();
    
    IGridCondition getSignPositiveCondition();

    ColumnGroup getGroup();

    Map<?, ImageBase64> getIconMap();

    int getIndex();

    LookupGridInfo getLookupGridInfo();

    Integer getMaxLength();

    List<Style> getStyles();

    ColumnType getType();

    Map<?, ILocalizedString> getValueSet();

    int getWidth();

    ColumnWrapType getWordWrap();

    boolean isComparable(IColumn column);

    boolean isEnumerated();

    boolean isFiltered();

    boolean isForUpdate();

    boolean isFrozen();

    boolean isHidden();

    boolean isVisible();

    void applyConfig(ColumnCfg config);
    
    Object setData(String key, Object value);
    
    void replaceStyles(List<ConditionalCellStyle> gridStyles);
    
    IValueSelectorConfig<ObjectType> getValueSelectorConfig();
}
