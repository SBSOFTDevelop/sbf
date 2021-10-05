package ru.sbsoft.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbsoft.common.Strings;
import ru.sbsoft.meta.ColumnsInfo;
import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.meta.columns.ColumnKind;
import ru.sbsoft.meta.columns.consts.ColumnDefinitions;
import ru.sbsoft.meta.columns.consts.Properties;
import ru.sbsoft.meta.columns.consts.PropertiesEnum;
import ru.sbsoft.meta.filter.FilterColumnConfig;
import ru.sbsoft.meta.filter.FilterDefinitionConfig;
import ru.sbsoft.meta.filter.LookupFilterDefinitionConfig;
import ru.sbsoft.meta.lookup.LookupValueProvider;
import ru.sbsoft.shared.Condition;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FiltersBean;
import ru.sbsoft.shared.Modifier;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;
import ru.sbsoft.shared.api.i18n.i18nUtils;
import ru.sbsoft.shared.consts.BrowserMode;
import ru.sbsoft.shared.consts.Formats;
import ru.sbsoft.shared.exceptions.FilterRequireException;
import ru.sbsoft.shared.interfaces.FilterEditorType;
import ru.sbsoft.shared.interfaces.GridType;
import ru.sbsoft.shared.interfaces.LookupType;
import ru.sbsoft.shared.interfaces.LookupTypeFake;
import ru.sbsoft.shared.interfaces.NamedGridType;
import ru.sbsoft.shared.interfaces.NamedItem;
import ru.sbsoft.shared.interfaces.NumeratedItem;
import ru.sbsoft.shared.meta.filter.EditorFilterDefinition;
import ru.sbsoft.shared.meta.filter.FilterDefinition;
import ru.sbsoft.shared.meta.filter.FilterDefinitions;
import ru.sbsoft.shared.meta.filter.FilterTemplate;
import ru.sbsoft.shared.meta.filter.FilterTemplateConfig;
import ru.sbsoft.shared.meta.filter.FilterTemplateItem;
import ru.sbsoft.shared.meta.filter.LookupFilterDefinition;
import ru.sbsoft.shared.meta.filter.LookupKeyType;
import ru.sbsoft.shared.model.SortDirection;
import ru.sbsoft.shared.model.SortingInfo;

/**
 * Общий темплейт для формирования метаинформации для грида
 *
 * @author sokolov
 */
public abstract class CommonTemplate implements Formats, ColumnDefinitions, IMetaTemplateInfo {

    private static final String TEMPALTE = "TEMPALTE";

    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    protected Set modifiers = Collections.EMPTY_SET;
    protected Map<String, Object> parameters;
    private List<FilterInfo> parentFilters =  Collections.emptyList();
    //
    private final Properties properties = new Properties();
    //
    protected ColumnsInfo columnsInfo;
    protected FilterTemplate filterTemplate;
    protected FilterDefinitions filterDefinitions;

    private Map<String, IFilterWrapper> filterWrappers = new HashMap<>();

    private List<SortingInfo> defaultSort = null;
    private boolean sortCleaned = false;

    private final Set<Enum> sysModes = new HashSet<>();
    private final Map<Class<? extends Enum>, Set<? extends Enum>> modes = new HashMap<>();

    private boolean forHistory = false;
    private boolean forSelect = false;

    public CommonTemplate() {
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    public void init(Object... values) {
        if (values != null) {
            final int length = values.length;
            if (length > 0) {
                for (byte i = 0; i < values.length; i++) {
                    setParam(i, values[i]);
                }
            }
        }
    }

    public void setModifiers(Set modifiers) {
        this.modifiers = Collections.EMPTY_SET;
        modes.clear();
        if (modifiers != null && !modifiers.isEmpty()) {
            for (Object mod : modifiers) {
                addModifier(mod);
            }
        }
        for (Enum mod : sysModes) {
            addMode(mod);
        }
    }

    public void addModifier(Object mod) {
        addModifier(mod, true);
    }

    private void addModifier(Object mod, boolean andMode) {
        if (mod != null) {
            if (modifiers == null || modifiers.isEmpty()) {
                modifiers = new HashSet();
            }
            modifiers.add(mod);
            if (andMode) {
                if (mod instanceof Enum) {
                    addMode((Enum) mod, false);
                } else {
                    log.warn("Not Enum modifier is found. It's not added as mode. " + mod.getClass().getName() + " -> " + mod);
                }
            }
        }
    }

    protected void setParam(byte index, Object value) {
    }

    protected List<FilterInfo> getParentFilters() {
        return parentFilters;
    }

    protected <T> T getParentFilterValue(String name, Class<T> valClass) {
        if (getParentFilters() == null || name == null || valClass == null) {
            return null;
        }
        for (FilterInfo f : getParentFilters()) {
            if (name.equals(f.getColumnName()) && f.getValue() != null && valClass.isAssignableFrom(f.getValue().getClass())) {
                return (T) f.getValue();
            }
        }
        return null;
    }

    public void setParentFilters(List<FilterInfo> parentFilters) {
        this.parentFilters = parentFilters != null ? parentFilters : Collections.<FilterInfo>emptyList();
    }
    
    private static boolean same(List<FilterInfo> pf1, List<FilterInfo> pf2) {
        if (pf1.size() != pf2.size()) {
            return false;
        }
        for (int i = 0; i < pf1.size(); i++) {
            if (!pf1.get(i).equals(pf2.get(i))) {
                return false;
            }
        }
        return true;
    }

    public void checkFilters(FiltersBean filters) throws FilterRequireException {
        for (FilterTemplateItem templateItem : getFilterTemplate().getItems()) {
            if (templateItem.getRequired()) {
                final FilterInfo filterInfo = filters.getSysCache().get(templateItem.getAlias());
                if (filterInfo == null || filterInfo.getValue() == null) {
                    throw new FilterRequireException("Mandatory fields not filled!");
                }
            }
        }
    }

    private void makeTemplate() {
        if (columnsInfo == null) {
            columnsInfo = createColumns();
            columnsInfo.setTemplateName(this.getClass().getName());
            sortCleaned = false;
        }
        if (filterDefinitions == null || filterTemplate == null) {
            filterDefinitions = new FilterDefinitions();
            filterTemplate = new FilterTemplate();
            defineFilters();
            sortCleaned = false;
        }
    }

    protected abstract ColumnsInfo createColumns();

    @Override
    public ColumnsInfo getColumnsInfo() {
        makeTemplate();
        return columnsInfo;
    }

    public LookupValueProvider createLookupProvider() {
        return null;
    }

    protected Map<String, Object> getParameters() {
        if (parameters == null) {
            parameters = new HashMap<>();
        }
        return parameters;
    }

    public Object getParameter(final String name) {
        if (parameters != null) {
            return parameters.get(name);
        }
        return null;
    }

    protected void addParameter(final String name, final Object value) {
        if (value instanceof ILocalizedString) {
            getParameters().put(name, i18nUtils.localizedStringToString((ILocalizedString) value));
        } else {
            getParameters().put(name, value);
        }
    }

    @Override
    public List<SortingInfo> getDefaultSort() {
        cleanDefaultSort();
        return defaultSort;
    }

    public final void setDefaultSort(String defaultSort) {
        this.defaultSort = parseSort(defaultSort);
        sortCleaned = false;
    }

    private class SQLNameParts {

        private final String table;
        private final String name;

        public SQLNameParts(String table, String name) {
            this.table = table;
            this.name = name;
        }

        public String getTable() {
            return table;
        }

        public String getName() {
            return name;
        }

    }

    private SQLNameParts parseSQLName(String s) {
        if (s == null || (s = s.trim()).isEmpty()) {
            return null;
        }
        String[] nameParts = s.split("\\.");
        String nameTable = nameParts.length > 1 ? nameParts[0] : null;
        if (nameTable != null && (nameTable = nameTable.trim()).isEmpty()) {
            nameTable = null;
        }
        String name = nameParts.length > 1 ? nameParts[1] : nameParts[0];
        name = name != null && !(name = name.trim()).isEmpty() ? name : null;
        return name != null ? new SQLNameParts(nameTable, name) : null;
    }

    private void cleanDefaultSort() {
        if (!sortCleaned && defaultSort != null && !defaultSort.isEmpty()) {
            Map<String, String> aliases = new HashMap<>();
            Map<String, String> clauses = new HashMap<>();
            for (ColumnInfo c : getColumnsInfo().getItems()) {
                String alias = c.getAlias();
                if (alias != null && !alias.trim().isEmpty()) {
                    aliases.put(alias.trim().toUpperCase(), alias);
                    String clause = c.getClause();
                    if (clause != null && !(clause = clause.trim()).isEmpty()) {
                        clauses.put(clause.toUpperCase(), alias);
                    }
                }
            }
            List<SortingInfo> cleaned = new ArrayList<>();
            for (SortingInfo inf : defaultSort) {
                String colName = inf.getAlias();
                if (colName != null && !(colName = colName.trim()).isEmpty()) {
                    colName = colName.toUpperCase();
                    String correctAlias = aliases.get(colName);
                    if (correctAlias == null) {
                        correctAlias = clauses.get(colName);
                    }
                    if (correctAlias == null) {
                        SQLNameParts nameParts = parseSQLName(colName);
                        if (nameParts != null) {
                            for (ColumnInfo c : getColumnsInfo().getItems()) {
                                SQLNameParts clauseParts = parseSQLName(c.getClause());
                                if (clauseParts != null && ((nameParts.getTable() == null) ^ (clauseParts.getTable() == null)) && nameParts.getName().equals(clauseParts.getName())) {
                                    correctAlias = c.getAlias();
                                    break;
                                }
                            }
                        }
                    }
                    if (correctAlias != null) {
                        inf.setAlias(correctAlias);
                    }
                    cleaned.add(inf);
                }
            }
            defaultSort = !cleaned.isEmpty() ? cleaned : null;
            sortCleaned = true;
        }
    }

    private List<SortingInfo> parseSort(String sort) {
        if (sort != null) {
            final String[] columns = sort.split(",");
            List<SortingInfo> res = new ArrayList<>();
            for (String column : columns) {
                column = Strings.clean(column);
                if (column != null) {
                    column = column.toUpperCase();
                    SortDirection dir = column.endsWith(" " + SortDirection.DESC.name()) ? SortDirection.DESC : SortDirection.ASC;
                    if (column.endsWith(" " + dir.name())) {
                        column = column.substring(0, column.length() - dir.name().length() - 1).trim();
                    }
                    if (column != null) {
                        column = column.trim();
                        if (!column.isEmpty()) {
                            res.add(new SortingInfo(column, dir));
                        }
                    }
                }
            }
            return res;
        }
        return null;
    }

    public boolean isSkipWhereOnGetRow() {
        return !properties.contains(PropertiesEnum.INCLUDE_WHERE_ON_GET_ROW);
    }

    public void setSkipWhereOnGetRow(boolean skipWhereOnGetRow) {
        properties.put(PropertiesEnum.INCLUDE_WHERE_ON_GET_ROW, skipWhereOnGetRow ? null : Boolean.TRUE);
    }

    public void setCaption(String caption) {
        properties.put(PropertiesEnum.CAPTION, caption);
    }

    protected <T extends Modifier> T findGridMode(Set modifiers, T def) {
        final Object[] mList = modifiers.toArray();
        for (Object m : mList) {
            if (m != null && m.getClass() == def.getClass()) {
                return (T) m;
            }
        }
        return def;
    }

    private static String getContextName(Class class$) {
        String result = class$.getSimpleName();
        if (result.endsWith(TEMPALTE)) {
            result = result.substring(0, result.length() - TEMPALTE.length());
        }
        return result;
    }

    @Override
    public final FilterDefinitions getFilterDefinitions() {
        makeTemplate();
        return filterDefinitions;
    }

    @Override
    public final IFilterWrapper getFilterWrapper(String name) {

        return filterWrappers.get(name);
    }

    /**
     * Метод для определения фильтров путем вызова методов {@code defineFilter}
     */
    protected void defineFilters() {
    }

    private <T extends FilterDefinition> T defineFilter(T def) {
        filterDefinitions.add(def);
        return def;
    }

    /**
     * Определение фильтра на основании колонки.
     *
     * @param alias идентификатор колонки
     * @return
     */
    protected final FilterTemplateConfig defineFilter(String alias) {
        return defineColumnFilterImpl(alias);
    }

    private FilterTemplateItem defineColumnFilterImpl(String alias) {
        ColumnInfo c = getColumnsInfo().get(alias);
        if (c == null) {
            throw new IllegalArgumentException("Can't define column filter: column not defined for alias: " + alias);
        }
        return initFilter(alias);
    }

    /**
     * Определение фильтра заданного типа. Не требует регистрации редактора в
     * клиентском коде.
     *
     * @param filterEditorType предоставляет идентификатор и заголовок
     * @param type тип фильтра
     * @param clause SQL-выражение фильтра
     * @return объект предоставляющий возможность конфигурации определенного
     * фильтра
     * @see #defineFilter(java.lang.String, ru.sbsoft.shared.meta.ColumnType,
     * java.lang.String, java.lang.String)
     */
    protected final FilterColumnConfig defineFilter(FilterEditorType filterEditorType, ColumnKind type, String clause) {
        return defineFilter(filterEditorType.getCode(), type, clause, filterEditorType.getTitle());
    }

    /**
     * Определение фильтра заданного типа. Не требует регистрации редактора в
     * клиентском коде.
     *
     * @param alias идентификатор
     * @param type тип фильтра
     * @param clause SQL-выражение фильтра
     * @param caption заголовок
     * @return объект предоставляющий возможность конфигурации определенного
     * фильтра
     */
    protected final FilterColumnConfig defineFilter(String alias, ColumnKind type, String clause, ILocalizedString caption) {
        ColumnInfo c = getColumnsInfo().get(alias);
        if (c != null) {
            throw new IllegalArgumentException("Can't define filter: column definition is found for alias: " + alias);
        }
        ColumnInfo col = getColumnsInfo().add(type, caption, clause, alias).hide();
        return new FilterColumnConfigImpl(defineColumnFilterImpl(alias), col);
    }

    /**
     * Определение фильтра заданного типа. Не требует регистрации редактора в
     * клиентском коде.
     *
     * @param alias идентификатор
     * @param type тип фильтра
     * @param clause SQL-выражение фильтра
     * @param caption заголовок
     * @return объект предоставляющий возможность конфигурации определенного
     * фильтра
     */
    protected final FilterColumnConfig defineFilter(String alias, ColumnKind type, String clause, String caption, IFilterWrapper wrapper) {
        if (wrapper != null) {
            filterWrappers.put(alias, wrapper);
        }

        return defineFilter(alias, type, clause, new NonLocalizedString(caption));
    }

    /**
     * Определение фильтра заданного типа. Не требует регистрации редактора в
     * клиентском коде.
     *
     * @param alias идентификатор
     * @param type тип фильтра
     * @param clause SQL-выражение фильтра
     * @param caption заголовок
     * @return объект предоставляющий возможность конфигурации определенного
     * фильтра
     */
    protected final FilterColumnConfig defineFilter(String alias, ColumnKind type, String clause, String caption) {
        return defineFilter(alias, type, clause, new NonLocalizedString(caption));
    }

    /**
     * Определение фильтра. Требует регистрации редактора в клиентском коде.
     *
     * @param filterEditorType идентификатор фильтра для регистрации редактора.
     * Предоставляет также заголовок.
     * @param template SQL-выражение фильтра. Знак :? заменяется на параметр с
     * именем идентификатора фильтра.
     * @param additionArgs если заданы, то к {@code template} применяется
     * {@link String#format(java.lang.String, java.lang.Object...)} с этими
     * аргументами
     * @return объект предоставляющий возможность конфигурации определенного
     * фильтра
     * @see
     * ru.sbsoft.client.components.browser.filter.editor.FilterEditorFactory
     */
    protected final FilterDefinitionConfig defineFilter(FilterEditorType filterEditorType, String template, Object... additionArgs) {
        final EditorFilterDefinition def = new EditorFilterDefinition();
        def.setAlias(filterEditorType.getCode());
        def.setFilterEditorType(filterEditorType);
        boolean noAliasSign = !(template.contains(":?") || template.contains("$?"));
        final String clause;
        if (additionArgs == null || additionArgs.length == 0) {
            if (noAliasSign) {
                clause = String.format(template, filterEditorType.getCode());
            } else {
                clause = template;
            }
        } else {
            final Object[] args;
            if (noAliasSign) {
                args = new Object[additionArgs.length + 1];
                args[0] = filterEditorType.getCode();
                System.arraycopy(additionArgs, 0, args, 1, additionArgs.length);
            } else {
                args = additionArgs;
            }
            clause = String.format(template, args);
        }
        def.setClause(clause);
        return new FilterDefConfigImpl(defineFilter(def), initFilter(filterEditorType.getCode()));
    }

    /**
     * Определяет lookup-фильтр. Требует регистрации редактора в клиентском
     * коде.
     *
     * @param lookupType идентификатор фильтра для регистрации редактора.
     * Предоставляет также заголовок.
     * @param clause выражение с которым сравниваются значения фильтра
     * @param miltiselect true - множественный выбор значений, false - выбор
     * одного значения
     * @return объект предоставляющий возможность конфигурации определенного
     * фильтра
     */
    protected final LookupFilterDefinitionConfig defineFilter(LookupType lookupType, String clause, boolean miltiselect) {
        return defineFilter(lookupType, clause, null, miltiselect);
    }

    /**
     * Определяет lookup-фильтр. Не требует регистрации редактора в клиентском
     * коде.
     *
     * @param filterAlias идентификатор фильтра
     * @param clause выражение с которым сравниваются значения фильтра
     * @param browserType идентификатор браузера. Предоставляет также заголовок.
     * @param miltiselect true - множественный выбор значений, false - выбор
     * одного значения
     * @return объект предоставляющий возможность конфигурации определенного
     * фильтра
     */
    protected final LookupFilterDefinitionConfig defineFilter(String filterAlias, String clause, NamedGridType browserType, boolean miltiselect) {
        return defineFilter(filterAlias, browserType.getItemName(), clause, browserType, miltiselect);
    }

    /**
     * Определяет lookup-фильтр. Не требует регистрации редактора в клиентском
     * коде.
     *
     * @param filterAlias идентификатор фильтра
     * @param filterTitle заголовок фильтра
     * @param clause выражение с которым сравниваются значения фильтра
     * @param browserType идентификатор браузера. Предоставляет также заголовок.
     * @param miltiselect true - множественный выбор значений, false - выбор
     * одного значения
     * @return объект предоставляющий возможность конфигурации определенного
     * фильтра
     */
    protected final LookupFilterDefinitionConfig defineFilter(String filterAlias, ILocalizedString filterTitle, String clause, NamedGridType browserType, boolean miltiselect) {
        return defineFilter(new LookupTypeFake(filterAlias, filterTitle), clause, browserType, miltiselect);
    }

    /**
     * Определяет lookup-фильтр. Не требует регистрации редактора в клиентском
     * коде.
     *
     * @param filterAlias идентификатор фильтра
     * @param filterTitle заголовок фильтра
     * @param clause выражение с которым сравниваются значения фильтра
     * @param browserType идентификатор браузера. Предоставляет также заголовок.
     * @param miltiselect true - множественный выбор значений, false - выбор
     * одного значения
     * @return объект предоставляющий возможность конфигурации определенного
     * фильтра
     */
    protected final LookupFilterDefinitionConfig defineFilter(String filterAlias, String filterTitle, String clause, NamedGridType browserType, boolean miltiselect) {
        return defineFilter(filterAlias, new NonLocalizedString(filterTitle), clause, browserType, miltiselect);
    }

    /**
     * Определяет lookup-фильтр. Не требует регистрации редактора в клиентском
     * коде.
     *
     * @param lookupType идентификатор фильтра. Предоставляет также заголовок.
     * @param clause выражение с которым сравниваются значения фильтра
     * @param browserType идентификатор браузера. Предоставляет также заголовок.
     * @param miltiselect true - множественный выбор значений, false - выбор
     * одного значения
     * @return объект предоставляющий возможность конфигурации определенного
     * фильтра
     */
    protected final LookupFilterDefinitionConfig defineFilter(LookupType lookupType, String clause, NamedGridType browserType, boolean miltiselect) {
        LookupFilterDefinition def = new LookupFilterDefinition();
        def.setGridContext(getContextName(this.getClass()));
        def.setAlias(lookupType.getCode());
        def.setClause(clause);
        def.setLookupType(lookupType);
        def.setMultiSelect(miltiselect);
        def.setBrowserType(browserType);
        return new LookupFilterDefConfigImpl(defineFilter(def), initFilter(lookupType.getCode()));
    }

    @Override
    public final FilterTemplate getFilterTemplate() {
        makeTemplate();
        return filterTemplate;
    }

    /**
     * Создать элемент фильтра основывающимся на существующей колонке
     *
     * @param alias - алиас объекта
     * @return
     */
    private FilterTemplateItem initFilter(String alias) {
        return filterTemplate.addItem(new FilterTemplateItem(alias));
    }

    public static void buildLookupSql(StringBuilder buffer, String clause, LookupKeyType type, String param) {
        buildLookupSql(buffer, clause, type, param + "_1", param + "_2");
    }

    public static void buildLookupSql(StringBuilder buffer, String clause, LookupKeyType type, String paramName1, String paramName2) {
        buffer.append(clause).append(" ");
        buffer.append("IN (");
        buffer.append("select Z.").append(type == LookupKeyType.ENTITY ? "LOOKUP_ENTITY_ID" : "LOOKUP_RECORD_ID");
        buffer.append(" from SYS_FILTER_LOOKUP Z");
        buffer.append(" where Z.SYS_OBJECT_STORAGE_RECORD_ID = :").append(paramName1);
        buffer.append(" and Z.SYS_FILTER_NUM = :").append(paramName2);
        buffer.append(")");
    }

    //================ MODE =================
    public void addSysMode(Enum mode) {
        sysModes.add(mode);
        addMode(mode);
    }

    protected <E extends Enum<E>> void addMode(E mod) {
        addMode(mod, true);
    }

    private <E extends Enum<E>> void addMode(E mod, boolean andModifier) {
        if (mod != null) {
            Class<E> modClass = (Class<E>) mod.getClass();
            Set<E> s = (Set<E>) modes.get(modClass);
            if (s == null) {
                s = EnumSet.noneOf(modClass);
                modes.put(modClass, s);
            }
            s.add(mod);
            if (andModifier) {
                addModifier(mod.name(), false);
            }
        }
        forHistory = isMode(BrowserMode.HISTORY);
        forSelect = isMode(BrowserMode.SELECT, BrowserMode.SELECT_EDIT);
    }

    protected final boolean isHistory() {
        return forHistory;
    }

    protected final boolean isSelect() {
        return forSelect;
    }

    protected final boolean isMode(Enum... modes) {
        for (Enum m : modes) {
            Set<? extends Enum> ms = this.modes.get(m.getClass());
            if (ms != null && ms.contains(m)) {
                return true;
            }
        }
        return false;
    }

    protected final <E extends Enum<E>> E getSingletonMode(Class<E> eClass) {
        Set<? extends Enum> sett = this.modes.get(eClass);
        if (sett != null && !sett.isEmpty()) {
            if (sett.size() > 1) {
                throw new IllegalStateException("There is many states of: " + eClass);
            }
            return eClass.cast(sett.iterator().next());
        }
        return null;
    }

    protected static String getParentParamName(GridType gridType) {
        return gridType.getParentIdName();
    }
    //=======================================

    private abstract static class FilterTemplateConfigImpl<SELF extends FilterTemplateConfig<?>> implements FilterTemplateConfig<SELF> {

        protected final FilterTemplateItem temp;

        protected FilterTemplateConfigImpl(FilterTemplateItem temp) {
            this.temp = temp;
        }

        @Override
        public SELF strict() {
            temp.strict();
            return (SELF) this;
        }

        @Override
        public SELF req() {
            temp.req();
            return (SELF) this;
        }

        @Override
        public SELF op(Condition condition) {
            temp.op(condition);
            return (SELF) this;
        }

    }

    private static class FilterDefConfigImpl<SELF extends FilterDefinitionConfig<?>, T extends FilterDefinition> extends FilterTemplateConfigImpl<SELF> implements FilterDefinitionConfig<SELF> {

        protected final T def;

        public FilterDefConfigImpl(T def, FilterTemplateItem temp) {
            super(temp);
            this.def = def;
        }

        @Override
        public SELF hide() {
            def.setHidden(true);
            return (SELF) this;
        }

        @Override
        public SELF nosql() {
            def.setHidden(true);
            def.setNoSql(true);
            return (SELF) this;
        }

    }

    private static class LookupFilterDefConfigImpl extends FilterDefConfigImpl<LookupFilterDefinitionConfig, LookupFilterDefinition> implements LookupFilterDefinitionConfig {

        public LookupFilterDefConfigImpl(LookupFilterDefinition def, FilterTemplateItem temp) {
            super(def, temp);
        }

        @Override
        public LookupFilterDefinitionConfig mod(Modifier modifier) {
            def.setModifier(modifier);
            return this;
        }

    }

    private static class FilterColumnConfigImpl extends FilterTemplateConfigImpl<FilterColumnConfig> implements FilterColumnConfig {

        private final ColumnInfo col;

        public FilterColumnConfigImpl(FilterTemplateItem temp, ColumnInfo col) {
            super(temp);
            this.col = col;
        }

        @Override
        public FilterColumnConfig vals(NamedItem... vals) {
            col.setValues(vals);
            return this;
        }

        @Override
        public FilterColumnConfig vals(NumeratedItem... vals) {
            col.setValues(vals);
            return this;
        }

        @Override
        public FilterColumnConfig descr(String s) {
            col.setToolTip(s);
            return this;
        }

        @Override
        public FilterColumnConfig setFormat(String format) {
            col.setFormat(format);
            return this;
        }

    }

}
