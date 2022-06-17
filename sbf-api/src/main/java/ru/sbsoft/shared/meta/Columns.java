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
public class Columns implements Serializable, IColumns {
    private List<IColumn> columns = new ArrayList<>();
    private String templateClassName;
    //
    private IColumn autoExpanColumn;
    private IColumn frozenAutoExpanColumn;
    //

    private String caption;
    private List<SortingInfo> sort;
    //
    private transient HashMap<String, IColumn> tmp;
    private transient IColumn primaryKey;
    //
    private List<Style> styles;
    private List<ConditionalCellStyle> gridStyles;
    private FilterDefinitions filterDefinitions;
    private FilterTemplate filterTemplate;
    //
    private IGridCondition editCondition;
    
    private boolean signPositive;

    private IGridCustomInfo customInfo;

    private String updateTableName;
    
    private boolean rowStyleDominate = true;

    public boolean addColumn(IColumn e) {
        return columns.add(e);
    }

    public boolean addColumns(Collection<? extends IColumn> c) {
        return columns.addAll(c);
    }

    public void clearColumns() {
        columns.clear();
    }

    @Override
    public List<IColumn> getColumns() {
        return columns;
    }

    @Override
    public IColumn getColumnForAlias(String alias) {
        if (tmp == null) {
            tmp = new HashMap<String, IColumn>();
            for (IColumn column : columns) {
                tmp.put(column.getAlias(), column);
            }
        }
        return tmp.get(alias);
    }

    @Override
    public IColumn getAutoExpanColumn() {
        return autoExpanColumn;
    }

    @Override
    public IColumn getFrozenAutoExpanColumn() {
        return frozenAutoExpanColumn;
    }

    public void add(IColumn column, boolean isAutoExpand) {
        columns.add(column);
        if (isAutoExpand) {
            if (column.isFrozen()) {
                frozenAutoExpanColumn = column;
            } else {
                autoExpanColumn = column;
            }
        }
    }

    @Override
    public List<SortingInfo> getSort() {
        return sort;
    }

    public void setSort(List<SortingInfo> sort) {
        this.sort = sort;
    }

    @Override
    public IColumn getPrimaryKey() {
        if (primaryKey == null) {
            for (IColumn column : columns) {
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

    @Override
    public IColumn getTemporalKey() {
        for (IColumn column : columns) {
            if (ColumnType.TEMPORAL_KEY.equals(column.getType())) {
                return column;
            }
        }
        return null;
    }

    public void setPrimaryKey(IColumn primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Override
    public List<Style> getStyles() {
        return styles;
    }

    @Override
    public boolean hasStyles() {
        if (getStyles() != null || getGridStyles() != null) {
            return true;
        }
        for (IColumn c : getColumns()) {
            if (c.getStyles() != null || c.getGridStyles() != null) {
                return true;
            }
        }
        return false;
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
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public FilterDefinitions getFilterDefinitions() {
        return filterDefinitions;
    }

    public void setFilterDefinitions(FilterDefinitions filterDefinitions) {
        this.filterDefinitions = filterDefinitions;
    }

    @Override
    public FilterTemplate getFilterTemplate() {
        return filterTemplate;
    }

    public void setFilterTemplate(FilterTemplate filterTemplate) {
        this.filterTemplate = filterTemplate;
    }

    @Override
    public String getTemplateClassName() {
        return templateClassName;
    }

    public void setTemplateClassName(String templateClassName) {
        this.templateClassName = templateClassName;
    }

    @Override
    public void applyColumnConfig(ColumnCfg config) {
        if (config == null || config.getAlias() == null) {
            return;
        }
        final IColumn column = getColumnForAlias(config.getAlias());
        if (column != null) {
            column.applyConfig(config);
        }
    }

    @Override
    public IGridCustomInfo getCustomInfo() {
        return customInfo;
    }

    public void setCustomInfo(IGridCustomInfo customInfo) {
        this.customInfo = customInfo;
    }

    @Override
    public boolean isSignPositive() {
        return signPositive;
    }

    public void setSignPositive(boolean signPositive) {
        this.signPositive = signPositive;
    }

    @Override
    public IGridCondition getEditCondition() {
        return editCondition;
    }

    public void setEditCondition(IGridCondition editCondition) {
        this.editCondition = editCondition;
    }

    @Override
    public boolean hasFrozen() {
        for (IColumn c : columns) {
            if (c.isFrozen()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int frozenWidth() {
        int width = 0;
        for (IColumn c : columns) {
            if (c.isFrozen()) {
                width += c.getWidth();
            }
        }
        return width;
    }

    @Override
    public boolean hasExpCellFormat() {
        for (IColumn c : getColumns()) {
            if (c.getExpCellFormat() != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getUpdateTableName() {
        return updateTableName;
    }

    public void setUpdateTableName(String updateTableName) {
        this.updateTableName = updateTableName;
    }

    @Override
    public void replaceStyles(List<ConditionalCellStyle> gridStyles) {
        setGridStyles(gridStyles);
    }

    @Override
    public boolean isRowStyleDominate() {
        return rowStyleDominate;
    }

    public void setRowStyleDominate(boolean rowStyleDominate) {
        this.rowStyleDominate = rowStyleDominate;
    }
}
