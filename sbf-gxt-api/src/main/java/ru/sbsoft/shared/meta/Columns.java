package ru.sbsoft.shared.meta;

import java.io.Serializable;
import java.util.*;
import ru.sbsoft.shared.grid.condition.IGridCondition;
import ru.sbsoft.shared.grid.style.ConditionalCellStyle;
import ru.sbsoft.shared.meta.config.ColumnCfg;
import ru.sbsoft.shared.meta.filter.FilterDefinitions;
import ru.sbsoft.shared.meta.filter.FilterTemplate;
import ru.sbsoft.shared.model.SortingInfo;

/**
 * Класс -контейнер колонок грида.
 *
 * @author balandin
 * @since Mar 17, 2014 12:20:47 PM
 */
public class Columns implements Serializable {

    private List<Column> columns = new ArrayList<Column>();
    private String templateClassName;
    //
    private Column autoExpanColumn;
    private Column frozenAutoExpanColumn;
    //

    private String caption;
    private List<SortingInfo> sort;
    //
    private transient HashMap<String, Column> tmp;
    private transient Column primaryKey;
    //
    private List<Style> styles;
    private List<ConditionalCellStyle> gridStyles;
    private FilterDefinitions filterDefinitions;
    private FilterTemplate filterTemplate;
    //
    private IGridCondition editCondition;

    private IGridCustomInfo customInfo;

    public boolean addColumn(Column e) {
        return columns.add(e);
    }

    public boolean addColumns(Collection<? extends Column> c) {
        return columns.addAll(c);
    }

    public void clearColumns() {
        columns.clear();
    }

    public List<Column> getColumns() {
        return columns;
    }

    public Column getColumnForAlias(String alias) {
        if (tmp == null) {
            tmp = new HashMap<String, Column>();
            int index = 0;
            for (Column column : columns) {
                column.setIndex(index++);
                tmp.put(column.getAlias(), column);
            }
        }
        return tmp.get(alias);
    }

    public Column getAutoExpanColumn() {
        return autoExpanColumn;
    }

    public Column getFrozenAutoExpanColumn() {
        return frozenAutoExpanColumn;
    }

    public void add(Column column, boolean isAutoExpand) {
        columns.add(column);
        if (isAutoExpand) {
            if (column.isFrozen()) {
                frozenAutoExpanColumn = column;
            } else {
                autoExpanColumn = column;
            }
        }
    }

    public List<SortingInfo> getSort() {
        return sort;
    }

    public void setSort(List<SortingInfo> sort) {
        this.sort = sort;
    }

    public Column getPrimaryKey() {
        if (primaryKey == null) {
            for (Column column : columns) {
                if (ColumnType.KEY.equals(column.getType())) {
                    primaryKey = column;
                    break;
                }
            }
            if (primaryKey == null) {
                throw new IllegalArgumentException("PK not found");
            }
        }
        return primaryKey;
    }

    public Column getTemporalKey() {
        for (Column column : columns) {
            if (ColumnType.TEMPORAL_KEY.equals(column.getType())) {
                return column;
            }
        }
        return null;
    }

    public void setPrimaryKey(Column primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<Style> getStyles() {
        return styles;
    }

    public boolean hasStyles() {
        if (getStyles() != null || getGridStyles() != null) {
            return true;
        }
        for (Column c : getColumns()) {
            if (c.getStyles() != null || c.getGridStyles() != null) {
                return true;
            }
        }
        return false;
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

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public FilterDefinitions getFilterDefinitions() {
        return filterDefinitions;
    }

    public void setFilterDefinitions(FilterDefinitions filterDefinitions) {
        this.filterDefinitions = filterDefinitions;
    }

    public FilterTemplate getFilterTemplate() {
        return filterTemplate;
    }

    public void setFilterTemplate(FilterTemplate filterTemplate) {
        this.filterTemplate = filterTemplate;
    }

    public String getTemplateClassName() {
        return templateClassName;
    }

    public void setTemplateClassName(String templateClassName) {
        this.templateClassName = templateClassName;
    }

    public void addColumnConfig(ColumnCfg config) {
        if (config == null || config.getAlias() == null) {
            return;
        }
        final Column column = getColumnForAlias(config.getAlias());
        if (null != column) {
            column.setConfig(config);
        }
    }

    public IGridCustomInfo getCustomInfo() {
        return customInfo;
    }

    public void setCustomInfo(IGridCustomInfo customInfo) {
        this.customInfo = customInfo;
    }

    public IGridCondition getEditCondition() {
        return editCondition;
    }

    public void setEditCondition(IGridCondition editCondition) {
        this.editCondition = editCondition;
    }

    public boolean hasFrozen() {
        for (Column c : columns) {
            if (c.isFrozen()) {
                return true;
            }
        }
        return false;
    }
    
    public int frozenWidth() {
        int width = 0;
        for (Column c : columns) {
            if (c.isFrozen()) {
                width += c.getWidth();
            }
        }
        return width;
    }

    public boolean hasExpCellFormat() {
        for (Column c : getColumns()) {
            if (c.getExpCellFormat() != null) {
                return true;
            }
        }
        return false;
    }
}
