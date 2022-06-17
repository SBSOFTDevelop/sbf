package ru.sbsoft.meta.columns;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.sbsoft.meta.columns.style.IColumnConditionFactory;
import ru.sbsoft.meta.columns.style.IGridConditionFactory;
import ru.sbsoft.meta.columns.style.condition.CNotNull;
import ru.sbsoft.meta.columns.style.condition.CNull;
import ru.sbsoft.server.utils.Image64Buffer;
import ru.sbsoft.shared.Condition;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.grid.condition.IGridCondition;
import ru.sbsoft.shared.grid.condition.NullComparison;
import ru.sbsoft.shared.grid.style.CStyle;
import ru.sbsoft.shared.grid.style.ConditionalCellStyle;
import ru.sbsoft.shared.interfaces.GridType;
import ru.sbsoft.shared.interfaces.NamedItem;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;
import ru.sbsoft.shared.grid.format.IExpCellFormat;
import ru.sbsoft.shared.interfaces.NumeratedItem;
import ru.sbsoft.shared.interfaces.ObjectType;
import ru.sbsoft.shared.meta.Aggregate;
import ru.sbsoft.shared.meta.CodebaseField;
import ru.sbsoft.shared.meta.Column;
import ru.sbsoft.shared.meta.ColumnGroup;
import ru.sbsoft.shared.meta.ColumnType;
import ru.sbsoft.shared.meta.ColumnWrapType;
import ru.sbsoft.shared.meta.IColumn;
import ru.sbsoft.shared.meta.IColumnCustomInfo;
import ru.sbsoft.shared.meta.IValueSelectorConfig;
import ru.sbsoft.shared.meta.LookupGridInfo;
import ru.sbsoft.shared.meta.LookupKeyType;
import ru.sbsoft.shared.meta.Style;
import ru.sbsoft.shared.meta.filter.Dictionary;
import ru.sbsoft.shared.model.ImageBase64;

/**
 * Абстрактный базовый обобщенный класс для всех *ColumnInfo классов.
 * <p>
 * Представляет информацию о колонке сетки.
 * <p>
 * Столбцы создаются экземпляром класса {@link ru.sbsoft.meta.ColumnsInfo}</p>
 * <p>
 * одним из перегружаемых методов  <code>public ColumnInfo add(...)</code>.
 * Например:
 * <pre>
 *  &#64;Override
 *    public ColumnsInfo createColumns() {
 *      ColumnsInfo c = new ColumnsInfo();
 *      c.add(KEY, "A.RECORD_ID");
 *      c.add(VCHAR, 100, "Период", "A.CALC_PERIOD");
 *      ...
 *    return c;
 * </pre>
 *
 * @author balandin
 * @param <T>
 */
public abstract class ColumnInfo<T> {

    private final ColumnType type;
    //
    protected String clause;
    protected String alias;
    protected ILocalizedString caption;
    protected int width;
    protected String nameClause;
    //
    protected boolean visible = true;
    protected boolean hidden = false;
    protected boolean filtered = true;
    protected String format;
    protected ILocalizedString toolTip;
    protected boolean autoExpand = false;
    protected boolean frozen = false;

    protected ColumnGroup group;
    protected boolean enumerated;
    protected LookupGridInfo lookupGridInfo;
    protected Dictionary dictionary;   
    protected List<Condition> filterConditions;
    protected List<String> combo;
    //
    private AbstractColumnFooter footer;
    private List<Style> styles;
    private final List<ConditionalCellStyle> gridStyles = new ArrayList<>();
    private IValueSelectorConfig<ObjectType> valueSelectorConfig;
    //
    private Map<T, URL> iconMap = null;

    private final Map<Object, ILocalizedString> valSet = new HashMap<>();

//    private UpdateInfo updateInfo;
    private boolean forUpdate = false;
    private Integer maxLength;
    //
    private IGridCondition editCondition;
    private IGridCondition signAllCondition;
    private IGridCondition signPositiveCondition;

//
    private IColumnCustomInfo customInfo;
    private IExpCellFormat expCellFormat;
//
    private ColumnWrapType wordWrap;

    public ColumnInfo(ColumnType type) {
        this.type = type;
    }

    public final ColumnType getType() {
        return type;
    }

    public ColumnWrapType getWordWrap() {
        return wordWrap;
    }

    public ColumnInfo<T> setWordWrap(ColumnWrapType wordWrap) {
        this.wordWrap = wordWrap;
        return this;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public ColumnInfo<T> setFrozen() {
        this.frozen = true;
        return this;
    }

    public IExpCellFormat getExpCellFormat() {
        return expCellFormat;
    }

    public ColumnInfo<T> setExpCellFormat(IExpCellFormat expCellFormat) {
        this.expCellFormat = expCellFormat;
        return this;
    }

    public String getClause() {
        return clause;
    }

    public void setClause(String clause) {
        this.clause = clause;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public ILocalizedString getCaption() {
        return caption;
    }

    public void setCaption(ILocalizedString caption) {
        this.caption = caption;
    }

    public void setCaption(String caption) {
        this.caption = new NonLocalizedString(caption);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getNameClause() {
        return nameClause;
    }

    public ColumnInfo<T> setNameClause(String nameClause) {
        this.nameClause = nameClause;
        return this;
    }

    /**
     * Метод вызывается построителем запросов экземпляром класса
     * {@link ru.sbsoft.meta.sql.SQLBuilder}.
     * <p>
     * Через вызов метода sb.append(String) происходит формирование строки SQL <code> select fields0 as alias,...fieldsN as alias
     * </code>.
     *
     * @param sb <code>StringBuilder</code>
     *
     */
    public void buildSelectClause(StringBuilder sb) {
        sb.append(clause);
        sb.append(" AS ");
        sb.append(alias);
    }

    public IColumn getColumn(int index) {
        Column result = new Column();
        result.setIndex(index);
        result.setCaption(caption);
        result.setDescription(toolTip);
        result.setAlias(alias);
        result.setType(type);
        result.setWidth(width);
        result.setGroup(group);
        result.setVisible(visible);
        result.setHidden(hidden);
        result.setFrozen(frozen);
        result.setFiltered(filtered);
        result.setFormat(format);
        result.setStyles(styles);
        result.setEnumerated(enumerated);
        result.setLookupGridInfo(lookupGridInfo);
        result.setDictionary(dictionary);
        result.setFilterConditions(filterConditions);
        if (iconMap != null && !iconMap.isEmpty()) {
            Image64Buffer imgBuf = Image64Buffer.getInst();
            Map<Object, ImageBase64> ico = new HashMap<>();
            for (Map.Entry<T, URL> e : iconMap.entrySet()) {
                try {
                    ico.put(e.getKey(), imgBuf.getImage64(e.getValue()));
                } catch (IOException ex) {
                    throw new RuntimeException("Can't get icons for column: " + getAlias(), ex);
                }
            }
            result.setIconMap(ico);
        }
        result.setGridStyles(gridStyles.isEmpty() ? null : gridStyles);
        if (!valSet.isEmpty()) {
            result.setValueSet(valSet);
        }
        result.setForUpdate(forUpdate);
        result.setMaxLength(maxLength);
        result.setCustomInfo(customInfo);
        result.setExpCellFormat(expCellFormat);
        result.setEditCondition(editCondition);
        result.setSignAllCondition(signAllCondition);
        result.setSignPositiveCondition(signPositiveCondition);
        result.setWordWrap(wordWrap);
        result.setCombo(combo);
        result.setValueSelectorConfig(valueSelectorConfig);

        return result;
    }

    public abstract T read(ResultSet resultSet) throws SQLException;

    public String getFormat() {
        return format;
    }

    public ColumnInfo<T> setFormat(String format) {
        this.format = format;
        return this;
    }

    public ILocalizedString getToolTip() {
        return toolTip;
    }

    public ColumnInfo<T> setToolTip(ILocalizedString toolTip) {
        this.toolTip = toolTip;
        return this;
    }

    public ColumnInfo<T> setToolTip(String toolTip) {
        return setToolTip(new NonLocalizedString(toolTip));
    }

    public boolean isAutoExpand() {
        return autoExpand;
    }

    public ColumnInfo<T> setAutoExpand(boolean value) {
        this.autoExpand = value;
        return this;
    }

    public boolean isVisible() {
        return visible;
    }

    public ColumnInfo<T> setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public boolean isFiltered() {
        return filtered;
    }

    public ColumnInfo<T> setFiltered(boolean filtered) {
        this.filtered = filtered;
        return this;
    }

    public ColumnInfo<T> noFlt() {
        return setFiltered(false);
    }

    public boolean isHidden() {
        return hidden;
    }

    public ColumnInfo<T> setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public ColumnInfo<T> hide() {
        return setHidden(true);
    }
    
    /**
     * Устанавливает перечень условий.
     * Если не определено, перечень условий уставнавливается по умолчанию для каждого типа
     */
    public ColumnInfo<T> setFilterConditions(Condition ... conditions) {
        this.filterConditions = Arrays.asList(conditions);
        return this;
    }

    public List<Condition> getFilterConditions() {
        return filterConditions;
    }
    
    public ColumnGroup getGroup() {
        return group;
    }

    public ColumnInfo<T> setGroup(ColumnGroup group) {
        this.group = group;
        return this;
    }

    public AbstractColumnFooter getFooter() {
        return footer;
    }

    public ColumnInfo<T> setFooter(Aggregate aggregates) {
        this.footer = new AggregateColumnFooter(aggregates, aggregates.getFormat() != null ? aggregates.getFormat() : format);
        return this;
    }

    public void setFooter(String expression) {
        this.footer = new ExpressionColumnFooter(expression);
    }

    public boolean hasFooter() {
        return footer != null;
    }

    public String getXlsTypeHint() {
        return "x:str";
    }

    /**
     * Добавляет стиль колонки, применяемый в зависимости от условия.
     *
     * @param style оъект, содержащий строку стиля в формате css и строку
     * условия на языке javascript
     * @return текущая колонка
     * @deprecated используйте
     * {@link #addStyle(ru.sbsoft.shared.grid.style.CStyle, ru.sbsoft.meta.columns.style.IColumnConditionFactory)}
     * или {@link #addStyle(ru.sbsoft.shared.grid.style.CStyle)}
     */
    public ColumnInfo addStyle(Style style) {
        if (styles == null) {
            styles = new ArrayList<>();
        }
        styles.add(style);
        return this;
    }

    public void enumerated() {
        this.enumerated = true;
    }

    public void enumerated(GridType lookupGridType) {
        enumerated(lookupGridType, null);
    }

    public void enumerated(GridType lookupGridType, LookupKeyType keyType) {
        LookupKeyType t = keyType;
        if (keyType == null) {
            t = LookupKeyType.KEY_NAME;
            if (getClause() != null && getClause().contains(CodebaseField.CODE_VALUE.getCode())) {
                t = LookupKeyType.KEY_CODE;
            }
        }
        this.lookupGridInfo = new LookupGridInfo(lookupGridType, t);
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void dictionary(GridType gridType) {
        this.dictionary = new Dictionary(gridType, null);
    }

    public void dictionary(GridType gridType, String column) {
        this.dictionary = new Dictionary(gridType, column);
    }

    /**
     * Задает графическое изображение(иконку) для определенного значения
     * {@code value} в колонке. Заданное изображение будет отображаться в гриде
     * на клиента вместо указанного значения.
     *
     * @param value значение колонки. Для корректной работы должно иметь
     * собственный метод {@link Object#equals(java.lang.Object)} и, желательно,
     * {@link Object#hashCode()}
     * @param iconFile адрес графического изображения, доступный с сервера
     * @return текущая колонка
     */
    public ColumnInfo<T> addDataIcon(T value, URL iconFile) {
        if (value == null) {
            throw new IllegalArgumentException("Value for data icon map can't be null");
        }
        if (iconFile == null) {
            throw new IllegalArgumentException("Data icon URL can't be null");
        }
        if (iconMap == null) {
            iconMap = new HashMap<>();
        }
        iconMap.put(value, iconFile);
        return this;
    }

    public Map<T, URL> getIconMap() {
        return iconMap;
    }

    /**
     * Добавляет безусловный (применяется всегда) стиль колонки.
     *
     * @param style стиль
     * @return текущая колонка
     */
    public ColumnInfo<T> addStyle(CStyle style) {
        return addStyle(style, (IGridCondition) null);
    }

    /**
     * Добавляет условный (применение зависит от {@code condition}) стиль
     * колонки. При этом объект условия не может содержать специфичных для
     * сервера (не доступных на клиенте) объектов. Метод определен как
     * внутренний, т.к. предполагается, что в прикладном смысле достаточно будет
     * {@link #addStyle(ru.sbsoft.shared.grid.style.CStyle, ru.sbsoft.meta.columns.style.IGridConditionFactory)}
     * и
     * {@link #addStyle(ru.sbsoft.shared.grid.style.CStyle, ru.sbsoft.meta.columns.style.IColumnConditionFactory)}
     *
     * @param style стиль
     * @param condition условие
     * @return текущая колонка
     */
    private ColumnInfo<T> addStyle(CStyle style, IGridCondition condition) {
        gridStyles.add(new ConditionalCellStyle(style, condition));
        return this;
    }

    /**
     * Добавляет условный (применение зависит от {@code conditionFactory}) стиль
     * колонки. При этом сгенерированный {@code conditionFactory} объект условия
     * не может содержать специфичных для сервера (не доступных на клиенте)
     * объектов. Этот метод предназначен для задания условия по колонкам
     * отличным от текущей. Для использования с этим методом имеются
     * библиотечные объекты:
     * {@link ru.sbsoft.meta.columns.style.condition.Eq}, {@link ru.sbsoft.meta.columns.style.condition.Gt}, {@link ru.sbsoft.meta.columns.style.condition.Substr}
     * и др. подобные.
     *
     * @param style стиль
     * @param conditionFactory генератор условия
     * @return текущая колонка
     */
    public ColumnInfo<T> addStyle(CStyle style, IGridConditionFactory conditionFactory) {
        return addStyle(style, conditionFactory != null ? conditionFactory.createCondition() : null);
    }

    /**
     * Добавляет условный (применение зависит от {@code conditionFactory}) стиль
     * колонки. При этом сгенерированный {@code conditionFactory} объект условия
     * не может содержать специфичных для сервера (не доступных на клиенте)
     * объектов. Этот метод предназначен для задания условия по текущей колонке.
     * Использование {@link IColumnConditionFactory} предполагает, что колонка
     * не задается приграммистом, а проставляется автоматически как текущая. Для
     * использования с этим методом имеются библиотечные объекты с префиксом 'C'
     * в имени:
     * {@link ru.sbsoft.meta.columns.style.condition.СEq}, {@link ru.sbsoft.meta.columns.style.condition.СGt}, {@link ru.sbsoft.meta.columns.style.condition.СSubstr}
     * и др. подобные.
     *
     * @param style стиль
     * @param conditionFactory генератор условия
     * @return текущая колонка
     */
    public ColumnInfo<T> addStyle(CStyle style, IColumnConditionFactory<T> conditionFactory) {
        if (conditionFactory != null) {
            conditionFactory.setColumn(this);
        }
        return addStyle(style, conditionFactory != null ? conditionFactory.createCondition() : null);
    }

    /**
     * Добавляет условный (применение зависит от {@code cmp}) стиль колонки.
     * Этот метод является другой записью метода
     * {@link #addStyle(ru.sbsoft.shared.grid.style.CStyle, ru.sbsoft.meta.columns.style.IColumnConditionFactory)}
     * при использовании с объектами {@link СNull} и {@link СNotNull}
     *
     * @param style стиль
     * @param cmp признак проверки значения колонки на NULL или NOT NULL для
     * применения стиля.
     * @return текущая колонка
     */
    public ColumnInfo<T> addStyle(CStyle style, NullComparison cmp) {
        return cmp != null ? addStyle(style, cmp == NullComparison.NULL ? new CNull() : new CNotNull()) : addStyle(style);
    }

    /**
     * Задает набор значений поля, представленного колонкой, при редактировании
     * фильтра. Для записи в поле берется числовое значение
     * {@link NumeratedItem#getNum()}, а для отображения пользователю -
     * {@link NumeratedItem#getItemName()}. Доступно только для колонок с типом
     * {@link ColumnType#BOOL}(только значения 1 и 0),
     * {@link ColumnType#INTEGER} или {@link ColumnType#VCHAR}(с преобразованием
     * числа в строку).
     *
     * @param vals фиксированный набор возможных значений
     * @return this
     */
    public ColumnInfo<T> setValues(NumeratedItem... vals) {
        ColumnType[] numTypes = {ColumnType.BOOL, ColumnType.INTEGER, ColumnType.VCHAR};
        if (notIn(numTypes)) {
            throw new IllegalArgumentException("NumeratedItem as values can be set only for column types: " + Arrays.asList(numTypes));
        }
        if (vals != null && vals.length > 0) {
            if (ColumnType.BOOL == type) {
                for (NumeratedItem v : vals) {
                    if (v.getNum() != 0 && v.getNum() != 1) {
                        throw new IllegalArgumentException("Num value for BOOL type can be only 0 or 1. Found: " + v.getNum());
                    }
                }
            }
            for (NumeratedItem val : vals) {
                if (ColumnType.BOOL == type) {
                    valSet.put(val.getNum() != 0, val.getItemName());
                } else {
                    valSet.put((long) val.getNum(), val.getItemName());
                }
            }
        }
        return this;
    }

    /**
     * Задает набор значений поля, представленного колонкой, при редактировании
     * фильтра. Для записи в поле берется строковое значение
     * {@link NamedItem#getCode()}, а для отображения пользователю -
     * {@link NumeratedItem#getItemName()}. Доступно только для колонок с типом
     * {@link ColumnType#VCHAR}.
     *
     * @param vals фиксированный набор возможных значений
     * @return this
     */
    public ColumnInfo<T> setValues(NamedItem... vals) {
        if (type != ColumnType.VCHAR) {
            throw new IllegalArgumentException("NamedItem as values can be set for column type VCHAR only");
        }
        if (vals != null && vals.length > 0) {
            for (NamedItem val : vals) {
                valSet.put((T) val.getCode(), val.getItemName());
            }
        }
        return this;
    }

    private boolean notIn(ColumnType... types) {
        for (ColumnType t : types) {
            if (type == t) {
                return false;
            }
        }
        return true;
    }
    
    public ColumnInfo<T> forUpdate(){
        forUpdate = true;
        return this;
    }

    public IGridCondition getEditCondition() {
        return editCondition;
    }

    public ColumnInfo<T> setEditCondition(IGridCondition editCondition) {
        this.editCondition = editCondition;
        return this;
    }
    
    public ColumnInfo<T> setEditCondition(IGridConditionFactory conditionFactory) {
        return setEditCondition(conditionFactory != null ? conditionFactory.createCondition() : null);
    }

    public ColumnInfo<T> setEditCondition(IColumnConditionFactory<T> conditionFactory) {
        if (conditionFactory != null) {
            conditionFactory.setColumn(this);
        }
        return setEditCondition(conditionFactory != null ? conditionFactory.createCondition() : null);
    }
    
    public IGridCondition getSignAllCondition() {
        return signAllCondition;
    }

    public ColumnInfo<T> setSignAllCondition(IGridCondition signAllCondition) {
        this.signAllCondition = signAllCondition;
        return this;
    }
    
    public ColumnInfo<T> setSignAllCondition(IGridConditionFactory conditionFactory) {
        return setSignAllCondition(conditionFactory != null ? conditionFactory.createCondition() : null);
    }    
    
    public ColumnInfo<T> setSignAllCondition(IColumnConditionFactory<T> conditionFactory) {
        if (conditionFactory != null) {
            conditionFactory.setColumn(this);
        }
        return setSignAllCondition(conditionFactory != null ? conditionFactory.createCondition() : null);        
    }

    public IGridCondition getSignPositiveCondition() {
        return signPositiveCondition;
    }

    public ColumnInfo<T> setSignPositiveCondition(IGridCondition signPositiveCondition) {
        this.signPositiveCondition = signPositiveCondition;
        return this;
    }

    public ColumnInfo<T> setSignPositiveCondition(IGridConditionFactory conditionFactory) {
        return setSignPositiveCondition(conditionFactory != null ? conditionFactory.createCondition() : null);
    }    
    
    public ColumnInfo<T> setSignPositiveCondition(IColumnConditionFactory<T> conditionFactory) {
        if (conditionFactory != null) {
            conditionFactory.setColumn(this);
        }
        return setSignPositiveCondition(conditionFactory != null ? conditionFactory.createCondition() : null);        
    }

    public IColumnCustomInfo getCustomInfo() {
        return customInfo;
    }

    public ColumnInfo<T> setCustomInfo(IColumnCustomInfo customInfo) {
        this.customInfo = customInfo;
        return this;
    }
    
    public ColumnInfo<T> setCombo(List<String> combo) {
        this.combo = combo;
        return this;
    }

    public ColumnInfo<T> setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public IValueSelectorConfig<?> getValueSelectorConfig() {
        return valueSelectorConfig;
    }

    public ColumnInfo<T> setValueSelectorConfig(IValueSelectorConfig<ObjectType> valueSelectorConfig) {
        this.valueSelectorConfig = valueSelectorConfig;
        return this;
    }
}
